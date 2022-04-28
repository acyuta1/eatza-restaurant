package com.eatza.shared.dto.restaurants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ReviewReceivedDto {

    private Long restaurantId;

    private Integer rating;

    private String userEmail;
}
