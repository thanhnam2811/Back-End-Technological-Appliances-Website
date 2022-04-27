package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.exception.ResourceNotFoundException;
import com.hcmute.backendtechnologicalapplianceswebsite.model.User;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/technological_appliances/")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get All Users
    @GetMapping("/users")
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    //    Create user
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    //    Get user by username
    @GetMapping("/users/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user =  userRepository.findById(username).
                orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return ResponseEntity.ok(user);
    }

    //    Update user
    @PutMapping("/users/{username}")
    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody User user) {
        User _user = userRepository.findById(username).
                orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        _user.setName(user.getName());
        _user.setEmail(user.getEmail());
        _user.setPhoneNumber(user.getPhoneNumber());
        _user.setAddress(user.getAddress());
        _user.setDateOfBirth(user.getDateOfBirth());
        _user.setGender(user.getGender());
        userRepository.save(_user);
        return ResponseEntity.ok(_user);
    }

    //    Delete user
    @DeleteMapping("/users/{username}")
    public ResponseEntity<User> deleteUser(@PathVariable String username) {
        User user = userRepository.findById(username).
                orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        userRepository.delete(user);
        return ResponseEntity.ok(user);
    }

}
