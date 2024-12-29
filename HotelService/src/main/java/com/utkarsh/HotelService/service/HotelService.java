package com.utkarsh.HotelService.service;

import com.utkarsh.HotelService.entity.Hotel;
import com.utkarsh.HotelService.exception.ResourceNotFoundException;
import com.utkarsh.HotelService.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HotelService implements IHotelService {
    private static final String NO_HOTEL_FOUND_FOR_ID = "No Hotel found for id: ";
    private final HotelRepository hotelRepository;

    @Autowired
    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public Hotel create(Hotel hotel) {
        String randomUuid = UUID.randomUUID().toString();
        hotel.setId(randomUuid);
        return hotelRepository.save(hotel);
    }

    @Override
    public List<Hotel> getAll() {
        return hotelRepository.findAll();
    }

    @Override
    public Hotel get(String id) {
        return hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(NO_HOTEL_FOUND_FOR_ID + id));
    }
}
