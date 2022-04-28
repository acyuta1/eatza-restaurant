package com.eatza.order.service.orderservice;

import com.eatza.order.dto.OrderUpdateDto;
import com.eatza.order.dto.OrderUpdateResponseDto;
import com.eatza.order.exception.OrderException;
import com.eatza.order.model.Order;
import com.eatza.order.model.OrderStatus;
import com.eatza.shared.dto.orders.OrderRequestDto;

import java.util.Optional;

public interface OrderService  {
	
	Order placeOrder(OrderRequestDto orderRequest) throws OrderException;
	boolean cancelOrder(Long orderId);
	Optional<Order> getOrderById(Long id);
	double getOrderAmountByOrderId(Long id);
	OrderUpdateResponseDto updateOrder(OrderUpdateDto orderUpdateRequest) throws OrderException;

	void updateOrder(Long orderId, String message, OrderStatus orderStatus, String subject);

//	void onDeliveryConfirm(Long orderId);
}
