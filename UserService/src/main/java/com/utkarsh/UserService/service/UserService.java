package com.utkarsh.UserService.service;

import com.utkarsh.UserService.entity.Rating;
import com.utkarsh.UserService.entity.User;
import com.utkarsh.UserService.exception.ResourceNotFoundException;
import com.utkarsh.UserService.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements IUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final String NO_USER_FOUND_FOR_ID = "No user found for id: ";
    private static final String USER_RATING_ENDPOINT = "http://localhost:8083/ratings/users/";
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public UserService(UserRepository userRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
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
        //Fetch user rating from the Rating Service
        //ratings/users/{userId}
        List<Rating> response = restTemplate.getForObject(USER_RATING_ENDPOINT + userId, ArrayList.class);
        user.setRatings(response);
        return user;
    }

    @Override
    public User deleteUser(String userId) {
        return null;
    }

    @Override
    public User updateUser(String userId, User user) {
        return null;
    }
}
