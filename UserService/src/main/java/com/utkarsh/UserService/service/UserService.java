package com.utkarsh.UserService.service;

import com.utkarsh.UserService.entity.Hotel;
import com.utkarsh.UserService.entity.Rating;
import com.utkarsh.UserService.entity.User;
import com.utkarsh.UserService.exception.ResourceNotFoundException;
import com.utkarsh.UserService.external.service.HotelService;
import com.utkarsh.UserService.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.utkarsh.UserService.utility.TypeConverter;

import java.util.*;

@Service
public class UserService implements IUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final String NO_USER_FOUND_FOR_ID = "No user found for id: ";
    private static final String USER_SERVICE_HOST = "http://RATING-SERVICE/";
    private static final String USER_RATING_ENDPOINT = "ratings/users/";
    private static final String HOTEL_SERVICE_HOST = "http://HOTEL-SERVICE/";
    private static final String HOTEL_ENDPOINT = "hotels/";

    private final UserRepository userRepository;
    private final HotelService hotelService;
    private final RestTemplate restTemplate;

    @Autowired
    public UserService(UserRepository userRepository, HotelService hotelService, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.hotelService = hotelService;
        this.restTemplate = restTemplate;
    }

    @Override
    public User saveUser(User user) {
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(NO_USER_FOUND_FOR_ID + userId));
        ArrayList response = restTemplate.getForObject(USER_SERVICE_HOST + USER_RATING_ENDPOINT + userId, ArrayList.class);
        List<Rating> userRatings = response.stream().map(res -> TypeConverter.convertLinkedHashMapToRating((LinkedHashMap<String, Object>) res)).toList();
        List<Rating> updatedUserRatings = userRatings.stream().map(rating -> {
//            ResponseEntity<Hotel> forEntity = restTemplate.getForEntity(HOTEL_SERVICE_HOST + HOTEL_ENDPOINT + rating.getHotelId(), Hotel.class);
//            Hotel hotel = forEntity.getBody();
            Hotel hotel = hotelService.getHotel(rating.getHotelId());
            rating.setHotel(hotel);
            return rating;
        }).toList();
        user.setRatings(userRatings);
        return user;
    }

    @Override
    public void deleteUser(String userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new ResourceNotFoundException(NO_USER_FOUND_FOR_ID + userId);
        }
        userRepository.deleteById(userId);
    }

    @Override
    public User updateUser(String userId, User user) {
        Optional<User> repoUserOptional = userRepository.findById(userId);
        if (repoUserOptional.isEmpty()) {
            throw new ResourceNotFoundException(NO_USER_FOUND_FOR_ID + userId);
        }
        User repoUser = repoUserOptional.get();
        updateUser(repoUser, user);
        return userRepository.save(repoUser);
    }

    private void updateUser(User existingUser, User updatedValue) {
        existingUser.setRatings(Objects.nonNull(updatedValue.getRatings()) ? updatedValue.getRatings() : existingUser.getRatings());
        existingUser.setAbout(Objects.nonNull(updatedValue.getAbout()) ? updatedValue.getAbout() : existingUser.getAbout());
        existingUser.setName(Objects.nonNull(updatedValue.getName()) ? updatedValue.getName() : existingUser.getName());
        existingUser.setEmail(Objects.nonNull(updatedValue.getEmail()) ? updatedValue.getEmail() : existingUser.getEmail());
    }
}
