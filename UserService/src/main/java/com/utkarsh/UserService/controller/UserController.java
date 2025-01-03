package com.utkarsh.UserService.controller;

import com.utkarsh.UserService.entity.User;
import com.utkarsh.UserService.payload.ApiResponse;
import com.utkarsh.UserService.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("/{userId}")
    @RateLimiter(name = "userRateLimiter", fallbackMethod = "ratingHotelFallback")
    @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    @Retry(name = "ratingHotelService", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getSingleUser(@PathVariable(name = "userId") String userId) {
        LOGGER.info("getting user info for userId: {}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(userId));
    }

    //fallback method for circuit breaker: should have same return type and should take param in the original method
    public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex) {
        LOGGER.info("Fallback is executed because service is down: {}", ex.getMessage());
        User user = new User();
        user.setName("dummy-user");
        user.setRatings(List.of());
        user.setAbout("Dummy-user created because some service is down");
        user.setUserId("random-string");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserValue(@PathVariable String id, @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .message("user with id: " + id + " deleted successfully").status(HttpStatus.OK)
                            .success(true)
                            .build(),
                    HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error occurred while deleting the user with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.builder()
                            .message("Error occurred while delete user with id: " + id)
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .success(false)
                            .build()
            );
        }
    }
}
