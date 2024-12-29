package com.utkarsh.RatingService.service;

import com.utkarsh.RatingService.entity.Rating;

import java.util.List;

public interface IRatingService {
    Rating create(Rating rating);

    List<Rating> getAllRatings();

    List<Rating> getRatingsByUserId(String userId);

    List<Rating> getRatingByHotelId(String hotelId);
}
