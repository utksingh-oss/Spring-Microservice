package com.utkarsh.UserService.utility;

import com.utkarsh.UserService.entity.Rating;

import java.util.LinkedHashMap;

public class TypeConverter {
    public static Rating convertLinkedHashMapToRating(LinkedHashMap<String, Object> linkedHashMap) {
        Rating rating = new Rating();
        rating.setHotelId((String) linkedHashMap.getOrDefault("hotelId", null));
        rating.setRating((Integer.parseInt(String.valueOf(linkedHashMap.getOrDefault("rating", null)))));
        rating.setFeedback((String) linkedHashMap.getOrDefault("feedback", null));
        rating.setRatingId((String) linkedHashMap.getOrDefault("ratingId", null));
        return rating;
    }
}
