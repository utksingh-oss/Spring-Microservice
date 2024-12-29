package com.utkarsh.HotelService.service;

import com.utkarsh.HotelService.entity.Hotel;

import java.util.List;

public interface IHotelService {
    Hotel create(Hotel hotel);

    List<Hotel> getAll();

    Hotel get(String id);
}
