package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.exception.ResourceNotFoundException;
import com.hcmute.backendtechnologicalapplianceswebsite.model.User;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/technological_appliances/")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    // Get All Users
    @GetMapping("users")
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    //    Create user
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return (User) userRepository.save(user);
    }

    //    Get user by username
    @GetMapping("/users/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user =  userRepository.findByUsername(username);
        if (user == null)
            throw new ResourceNotFoundException("User not found with username: " + username);
        return ResponseEntity.ok(user);
    }

    //    Update user
    @PutMapping("/users/{username}")
    public ResponseEntity<User> updateuser(@PathVariable String username, @RequestBody User user) throws Throwable {
        User _user = (User) userRepository.findByUsername(username);
        if (_user == null)
            throw new ResourceNotFoundException("User not found with username: " + username);
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
    public ResponseEntity<User> deleteuser(@PathVariable String username) throws Throwable {
        User user = (User) userRepository.findByUsername(username);
        if (user == null)
            throw new ResourceNotFoundException("User not found with username: " + username);
        userRepository.delete(user);
        return ResponseEntity.ok(user);
    }

}
