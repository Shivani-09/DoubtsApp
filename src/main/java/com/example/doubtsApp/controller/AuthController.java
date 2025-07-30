package com.example.doubtsApp.controller;

import com.example.doubtsApp.dto.LoginRequest;
import com.example.doubtsApp.dto.RegisterRequest;
import com.example.doubtsApp.dto.UpdateRequest;
import com.example.doubtsApp.model.User;
import com.example.doubtsApp.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return new ResponseEntity<>("Error: Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ResponseEntity<>("Error: Email is already in use!", HttpStatus.BAD_REQUEST);
        }

        User user = new User(registerRequest.getUsername(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()));

        userRepository.customSave(user); // Still using customSave

        return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("Error: User not found!", HttpStatus.UNAUTHORIZED);
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return new ResponseEntity<>("Error: Invalid password!", HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>("User logged in successfully!", HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateRequest userDetails) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("Error: User not found!", HttpStatus.NOT_FOUND);
        }

        User existingUser = userOptional.get();

        if (!existingUser.getUsername().equals(userDetails.getUsername()) && userRepository.existsByUsername(userDetails.getUsername())) {
            return new ResponseEntity<>("Error: Username is already taken by another user!", HttpStatus.BAD_REQUEST);
        }

        if (!existingUser.getEmail().equals(userDetails.getEmail()) && userRepository.existsByEmail(userDetails.getEmail())) {
            return new ResponseEntity<>("Error: Email is already in use by another user!", HttpStatus.BAD_REQUEST);
        }

        boolean changed = false;
        if (!existingUser.getUsername().equals(userDetails.getUsername())) {
             existingUser.setUsername(userDetails.getUsername());
             changed = true;
        }
        if (!existingUser.getEmail().equals(userDetails.getEmail())) {
            existingUser.setEmail(userDetails.getEmail());
            changed = true;
        }

        if (userDetails.getPassword() != null && !userDetails.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            changed = true;
        }

        if (changed) {
            userRepository.customSave(existingUser); // Still using customSave
        }

        return new ResponseEntity<>("User updated successfully!", HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return new ResponseEntity<>("Error: User not found!", HttpStatus.NOT_FOUND);
        }
        userRepository.deleteById(id);
        return new ResponseEntity<>("User deleted successfully!", HttpStatus.OK);
    }
}