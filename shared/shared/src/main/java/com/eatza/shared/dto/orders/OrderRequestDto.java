package com.eatza.shared.dto.orders;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
    private Long orderId;

    @NotNull(message = "customer.id.is.empty")
    private Long customerId;

    @NotNull(message = "restaurant.id.is.empty")
    private Long restaurantId;

    @NotNull(message = "items.empty")
    private List<OrderedItemsDto> items;
}