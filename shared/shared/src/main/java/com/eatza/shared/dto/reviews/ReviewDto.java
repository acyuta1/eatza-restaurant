package com.eatza.shared.dto.reviews;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {

    private Long id;

    @NotBlank(message = "review.is.empty")
    @Size(min = 10, max = 255)
    private String reviewText;

    @NotBlank(message = "rating.value.is.empty")
    private String rating;

    @NotNull(message = "order.id.empty")
    private Long orderId;

    @NotNull(message = "customerId.empty")
    private Long customerId;

}
