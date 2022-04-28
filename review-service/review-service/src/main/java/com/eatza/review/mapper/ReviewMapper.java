package com.eatza.review.mapper;

import com.eatza.review.model.Review;
import com.eatza.shared.dto.reviews.ReviewDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public abstract class ReviewMapper {

    @Mapping(target = "id", ignore = true)
    public abstract Review fromDto(ReviewDto reviewDto);

    public abstract ReviewDto toDto(Review review);
}
