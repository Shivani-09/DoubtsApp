package com.example.doubtsApp.repository;

import com.example.doubtsApp.model.User;

public interface CustomUserRepository {
    User customSave(User user);
}