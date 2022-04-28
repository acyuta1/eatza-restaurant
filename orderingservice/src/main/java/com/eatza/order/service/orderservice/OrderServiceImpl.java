package com.eatza.order.service.orderservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.eatza.order.messaging.producer.OrderInitiatedProducer;
import com.eatza.order.model.OrderStatus;
import com.eatza.shared.config.kafka.producer.sharedProducers.EmailNotificationProducer;
import com.eatza.shared.dto.EmailDto;
import com.eatza.shared.dto.orders.OrderDeclineDto;
import com.eatza.shared.dto.orders.OrderRequestDto;
import com.eatza.shared.dto.orders.OrderedItemsDto;
import com.eatza.shared.feign.customers.CustomerClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.eatza.order.dto.ItemFetchDto;
import com.eatza.order.dto.OrderUpdateDto;
import com.eatza.order.dto.OrderUpdateResponseDto;
import com.eatza.order.exception.OrderException;
import com.eatza.order.model.Order;
import com.eatza.order.model.OrderedItem;
import com.eatza.order.repository.OrderRepository;
import com.eatza.order.service.itemservice.ItemService;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {


	private final OrderRepository orderRepository;

	private final ItemService itemService;

	@Value("${restaurant.search.item.url}")
	private String restaurantServiceItemUrl;

	private final RestTemplate restTemplate;

	private final OrderInitiatedProducer orderInitiatedProducer;

	private final EmailNotificationProducer emailNotificationProducer;

	private final CustomerClient customerClient;

	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);


	@Override
	public Order placeOrder(OrderRequestDto orderRequest) throws OrderException {

		// status: initiated, will call restaurant to check if items are available
		// status: denied, if the restaurant does not have enough items, will result in refund
		// status: confirmed, if available
		// status: delivered, notified as delivered and receives review mail
		// if the user submits review, restaurant gets rating update.
		logger.debug("In place order method, creating order object to persist");
		Order order = new Order(orderRequest.getCustomerId(), OrderStatus.INITIATED, orderRequest.getRestaurantId());
		logger.debug("saving order in db");
		Order savedOrder = orderRepository.save(order);
		logger.debug("Getting all ordered items to persist");

		orderRequest.setOrderId(order.getId());

		// send order initatiated message to kafka, notifying restaurant
		orderInitiatedProducer.send(orderRequest);

		logger.debug("Saved order to db");
		return savedOrder;
	}

	@Override
	public boolean cancelOrder(Long orderId) {
		logger.debug("In cancel order service method, calling repository");
		Optional<Order> order = orderRepository.findById(orderId);
		if(order.isPresent()) {
			logger.debug("Order was found in db");
			order.get().setStatus(OrderStatus.DENIED);
			orderRepository.save(order.get());
			return true;
		}
		else {
			logger.debug("Order not found");
			return false;
		}

	}

	@Override
	public Optional<Order> getOrderById(Long id) {
		return orderRepository.findById(id);
	}

	@Override
	public double getOrderAmountByOrderId(Long id) {

		Optional<Order> order = orderRepository.findById(id);
		if(order.isPresent()) {
			List<OrderedItem> itemsOrdered= itemService.findbyOrderId(id);
			double total=0;
			for(OrderedItem item: itemsOrdered) {
				total = total+(item.getPrice()*item.getQuantity());
			}
			return total;
		}
		else {
			// handle exception here later.
			return 0;
		}


	}

	@Override
	public OrderUpdateResponseDto updateOrder(OrderUpdateDto orderUpdateRequest) throws OrderException {

		Order order = new Order(orderUpdateRequest.getCustomerId(), OrderStatus.UPDATED, orderUpdateRequest.getRestaurantId());
		Optional<Order> previouslyPersistedOrder = orderRepository.findById(orderUpdateRequest.getOrderId());

		if(!previouslyPersistedOrder.isPresent()) {
			// handle exception properly later
			throw new OrderException("Update Failed, respective order not found");
		}
		if(!(orderUpdateRequest.getRestaurantId().equals(previouslyPersistedOrder.get().getRestaurantId() ))) {
			// handle exception properly later
			throw new OrderException("Update Failed, cannot change restaurants while updating order");

		}
		List<OrderedItem> previouslyOrderedItems= itemService.findbyOrderId(previouslyPersistedOrder.get().getId());
		order.setId(previouslyPersistedOrder.get().getId());
		order.setCreateDateTime(previouslyPersistedOrder.get().getCreateDateTime());
		List<OrderedItemsDto> itemsDtoList = orderUpdateRequest.getItems();
		List<OrderedItem> updateItemsListToReturn = new ArrayList<>();
		for(OrderedItemsDto itemDto: itemsDtoList) {
			MappingJackson2HttpMessageConverter map = new MappingJackson2HttpMessageConverter();
			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			messageConverters.add(map);
			messageConverters.add(new FormHttpMessageConverter());
			restTemplate.setMessageConverters(messageConverters);
			try {
				ItemFetchDto item = restTemplate.getForObject(restaurantServiceItemUrl+itemDto.getItemId(), ItemFetchDto.class);
				if(item==null ) {
					// deleting previously updated items 
					for(OrderedItem itemsUpdateToBeReverted: updateItemsListToReturn) {
						itemService.deleteItemsById(itemsUpdateToBeReverted.getId());
					}
					throw new OrderException("Update Failed, item not found in menu");
				}
				if(item.getMenu().getRestaurant().getId()!=order.getRestaurantId() ) {
					// deleting previously updated items 
					for(OrderedItem itemsUpdateToBeReverted: updateItemsListToReturn) {
						itemService.deleteItemsById(itemsUpdateToBeReverted.getId());
					}
					throw new OrderException("Update Failed, item does not belong to respective restaurant");
				}

				if(itemDto.getQuantity()<=0 ) {
					// deleting previously updated items 
					for(OrderedItem itemsUpdateToBeReverted: updateItemsListToReturn) {
						itemService.deleteItemsById(itemsUpdateToBeReverted.getId());
					}
					throw new OrderException("Update Failed, quantity cannot be zero");
				}

				OrderedItem itemToPersist = new OrderedItem(item.getName(), itemDto.getQuantity(), item.getPrice(),previouslyPersistedOrder.get(), item.getId());
				itemToPersist.setId(itemDto.getItemId());
				OrderedItem savedItem = itemService.saveItem(itemToPersist);
				updateItemsListToReturn.add(savedItem);
			}
			catch(ResourceAccessException e) {
				throw new OrderException("Something went wrong, looks "
						+ "like restaurant is currently not operatable");
			}

		}
		for(OrderedItem previouslyOrderedItem: previouslyOrderedItems) {
			itemService.deleteItemsById(previouslyOrderedItem.getId());
		}
		Order savedOrder = orderRepository.save(order);

		return new OrderUpdateResponseDto(savedOrder.getId(), savedOrder.getCustomerId(), savedOrder.getStatus().toString(), savedOrder.getRestaurantId(),updateItemsListToReturn );


	}

	@Override
	public void updateOrder(Long orderId, String message, OrderStatus orderStatus, String subject) {
		var order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "order not present"));

		order.setStatus(orderStatus);

		orderRepository.save(order);

		var customer = customerClient.getCustomer(order.getCustomerId());

		var emailDto = new EmailDto()
				.setUserEmail(customer.getEmail())
				.setMessage(message)
				.setSubject(subject);
		emailNotificationProducer.send(emailDto);
	}
}
