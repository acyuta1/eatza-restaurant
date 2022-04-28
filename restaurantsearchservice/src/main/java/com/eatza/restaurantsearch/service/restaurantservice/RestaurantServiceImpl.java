package com.eatza.restaurantsearch.service.restaurantservice;

import com.eatza.restaurantsearch.dto.RestaurantRequestDto;
import com.eatza.restaurantsearch.dto.RestaurantResponseDto;
import com.eatza.restaurantsearch.exception.RestaurantNotFoundException;
import com.eatza.restaurantsearch.messaging.producer.OrderDeclineProducer;
import com.eatza.restaurantsearch.messaging.producer.OrderDeliveryConfirmProducer;
import com.eatza.restaurantsearch.model.Menu;
import com.eatza.restaurantsearch.model.MenuItem;
import com.eatza.restaurantsearch.model.Restaurant;
import com.eatza.restaurantsearch.repository.RestaurantRepository;
import com.eatza.restaurantsearch.service.menuitemservice.MenuItemService;
import com.eatza.restaurantsearch.service.menuservice.MenuService;
import com.eatza.shared.config.kafka.producer.sharedProducers.EmailNotificationProducer;
import com.eatza.shared.dto.EmailDto;
import com.eatza.shared.dto.customers.CustomerDto;
import com.eatza.shared.dto.customers.WalletOperation;
import com.eatza.shared.dto.customers.WalletUpdateDto;
import com.eatza.shared.dto.orders.OrderDeclineDto;
import com.eatza.shared.dto.orders.OrderRequestDto;
import com.eatza.shared.dto.orders.OrderedItemsDto;
import com.eatza.shared.dto.restaurants.ReviewReceivedDto;
import com.eatza.shared.feign.customers.CustomerClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final MenuService menuService;

    private final MenuItemService menuItemService;

    private final CustomerClient customerClient;

    private final OrderDeclineProducer orderDeclineProducer;

    private final OrderDeliveryConfirmProducer orderDeliveryConfirmProducer;

    private final EmailNotificationProducer emailNotificationProducer;

    @Override
    @Cacheable(value = "allrestaurants")
    public RestaurantResponseDto findAllRestaurants(int pageNumber, int pageSize) {

        log.debug("In find all restaurants, creating pageable"
                + " object for page Number:" + pageNumber + " and page size: " + pageSize);

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        log.debug("Calling repository to get all restaurants");
        Page<Restaurant> contentPage = restaurantRepository.findAll(pageable);
        List<Restaurant> restaurantsToReturn = contentPage.getContent();
        return new RestaurantResponseDto(restaurantsToReturn, contentPage.getTotalPages(), contentPage.getTotalElements());

    }


    @Override
    public Restaurant saveRestaurant(RestaurantRequestDto restaurantDto) {
        log.debug("In saveRestaurant, creating object of restaurant to save");
        Restaurant restaurant = new Restaurant(restaurantDto.getName(), restaurantDto.getLocation(),
                restaurantDto.getCuisine(), restaurantDto.getBudget(), restaurantDto.getRating());
        log.debug("calling repository save restaurant method");
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        Menu menu = new Menu(restaurantDto.getActiveFrom(), restaurantDto.getActiveTill(), savedRestaurant);
        menuService.saveMenu(menu);
        return savedRestaurant;

    }


    @Override
    @Cacheable(value = "restaurantbyname")
    public RestaurantResponseDto findByName(String name, int pageNumber, int pageSize) {
        log.debug("In findByName, creating pageable"
                + " object for page Number:" + pageNumber + " and page size: " + pageSize);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Restaurant> contentPage = restaurantRepository.findByNameContaining(name, pageable);
        List<Restaurant> restaurantsToReturn = contentPage.getContent();
        return new RestaurantResponseDto(restaurantsToReturn, contentPage.getTotalPages(), contentPage.getTotalElements());
    }


    @Override
    @Cacheable(value = "restaurantbylocandcuisine")
    public RestaurantResponseDto findByLocationAndCuisine(String location, String cuisine, int pageNumber, int pageSize) {
        log.debug("In findByLocationAndCuisine, creating pageable"
                + " object for page Number:" + pageNumber + " and page size: " + pageSize);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Restaurant> contentPage = restaurantRepository.findByLocationContainingAndCuisineContaining(location, cuisine, pageable);
        if (contentPage.isEmpty()) {
            contentPage = restaurantRepository.findByLocationContainingOrCuisineContaining(location, cuisine, pageable);
        }

        List<Restaurant> restaurantsToReturn = contentPage.getContent();
        return new RestaurantResponseDto(restaurantsToReturn, contentPage.getTotalPages(), contentPage.getTotalElements());


    }

    @Override
    @Cacheable(value = "restaurantbylocationandname")
    public RestaurantResponseDto findByLocationAndName(String location, String name, int pageNumber, int pageSize) {
        log.debug("In findByLocationAndName, creating pageable"
                + " object for page Number:" + pageNumber + " and page size: " + pageSize);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Restaurant> contentPage = restaurantRepository.findByLocationContainingAndNameContaining(location, name, pageable);
        if (contentPage.isEmpty()) {
            contentPage = restaurantRepository.findByLocationContainingOrNameContaining(location, name, pageable);
        }

        List<Restaurant> restaurantsToReturn = contentPage.getContent();
        return new RestaurantResponseDto(restaurantsToReturn, contentPage.getTotalPages(), contentPage.getTotalElements());


    }

    @Override
    @Cacheable(value = "restaurantbybudget")
    public RestaurantResponseDto findByBudget(int budget, int pageNumber, int pageSize) {
        log.debug("In findByBudget, creating pageable"
                + " object for page Number:" + pageNumber + " and page size: " + pageSize);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Restaurant> contentPage = restaurantRepository.findByBudgetLessThanEqual(budget, pageable);
        List<Restaurant> restaurantsToReturn = contentPage.getContent();
        return new RestaurantResponseDto(restaurantsToReturn, contentPage.getTotalPages(), contentPage.getTotalElements());


    }

    @Override
    @Cacheable(value = "restaurantbyrating")
    public RestaurantResponseDto findByRating(double rating, int pageNumber, int pageSize) {
        log.debug("In findByRating, creating pageable"
                + " object for page Number:" + pageNumber + " and page size: " + pageSize);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Restaurant> contentPage = restaurantRepository.findByRatingGreaterThanEqual(rating, pageable);
        List<Restaurant> restaurantsToReturn = contentPage.getContent();
        return new RestaurantResponseDto(restaurantsToReturn, contentPage.getTotalPages(), contentPage.getTotalElements());


    }


    @Override
    @Cacheable(value = "restaurantbyid")
    public Restaurant findById(Long id) {
        log.debug("In find by id method, calling repository");
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        if (optionalRestaurant.isPresent()) {
            return optionalRestaurant.get();
        } else {
            log.debug("No restaurant found for given Id");
            throw new RestaurantNotFoundException("No restaurant found for given Id");
        }

    }


    @Override
    public List<MenuItem> findMenuItemByRestaurantId(Long restaurantId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Menu menu = menuService.getMenuByRestaurantId(restaurantId);
        Page<MenuItem> menuItemsToReturn = menuItemService.findByMenuId(menu.getId(), pageable);
        if (menuItemsToReturn.hasContent()) {
            return menuItemsToReturn.get().collect(Collectors.toList());
        } else {
            throw new RestaurantNotFoundException("No items found for given restaurant ");
        }
    }

    @Override
    public void processOrderInit(OrderRequestDto orderRequestDto) {

        var itemIds = orderRequestDto
                .getItems()
                .stream()
                .map(OrderedItemsDto::getItemId)
                .collect(Collectors.toSet());

        var menuItems = menuItemService.findAllByIds(itemIds);

        if (menuItems.size() != itemIds.size()) {
            // order service, order status denied

            var message = "Items not available at the moment";
            log.error(message);

            declineOrder(
                    message,
                    orderRequestDto.getOrderId()
            );
            return;
        }

        var cost = menuItems
                .stream()
                .map(item -> {
                    var reqItemOpt = orderRequestDto.getItems().stream().filter(requestedItem -> item.getId().equals(requestedItem.getItemId()))
                            .findFirst();

                    if(reqItemOpt.isEmpty())
                        return 0;
                    var reqItem = reqItemOpt.get();

                    return item.getPrice() * reqItem.getQuantity();
                })
                .mapToInt(Integer::intValue)
                .sum();

        var customer = customerClient.getCustomer(orderRequestDto.getCustomerId());

        if (customer.getWalletAmount() < cost) {
            var message = String.format("Insufficient wallet amount to place this order. Wallet amount = %s, Required amount = %s", customer.getWalletAmount(), cost);
            log.error(message);

            declineOrder(
                    message,
                    orderRequestDto.getOrderId()
            );
            return;
        }

        // update wallet amount
        updateCustomerWallet(customer, cost);

        // Assume Delivery tracking is taking place.
        log.info("Order {} delivered successfully.", orderRequestDto.getOrderId());
        orderDeliveryConfirmProducer.send(orderRequestDto.getOrderId());
    }

    @Override
    public void processReviewReceived(ReviewReceivedDto reviewReceivedDto) {
        var restaurant = findById(reviewReceivedDto.getRestaurantId());

        // calculate new rating for the restaurant.
        var newRating = ((restaurant.getRating() * restaurant.getRatingCount()) + reviewReceivedDto.getRating()) / (restaurant.getRatingCount() + 1);

        restaurant.setRating(newRating);
        restaurant.setRatingCount(restaurant.getRatingCount() + 1);

        restaurantRepository.save(restaurant);

        // send thankyou mail to customer.
        var emailDto = new EmailDto()
                .setMessage("Thankyou for submitting feedback!")
                .setSubject(String.format("Message from %s", restaurant.getName()))
                .setUserEmail(reviewReceivedDto.getUserEmail());
        emailNotificationProducer.send(emailDto);

    }

    private void declineOrder(String reason, Long orderId) {
        var orderDeclineDto = new OrderDeclineDto();
        orderDeclineDto.setOrderId(orderId);
        orderDeclineDto.setReason(reason);

        orderDeclineProducer.send(orderDeclineDto);
    }

    private void updateCustomerWallet(CustomerDto customerDto, Integer amount) {
        // send email to customer.
        var walletUpdateDto = new WalletUpdateDto();
        walletUpdateDto.setCustomerId(customerDto.getId());
        walletUpdateDto.setAmount(amount);
        walletUpdateDto.setOperation(WalletOperation.DEDUCT);

        log.info("updating customer's ({}) wallet", customerDto.getId());
        customerClient.updateWallet(customerDto.getId(), walletUpdateDto);
    }
}
