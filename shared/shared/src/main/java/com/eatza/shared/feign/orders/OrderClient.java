package com.eatza.shared.feign.orders;

import com.eatza.shared.dto.orders.OrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ordering-service")
public interface OrderClient {

    @GetMapping("/order/{orderId}")
    ResponseEntity<OrderDto> getOrderById(@PathVariable("orderId") Long orderId);
}
