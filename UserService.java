package com.example.updatesql.service;

import com.example.updatesql.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    private final String[] allowedFields = {"username", "email", "phone"};

    public boolean updateUserField(int id, String field, String value) {
        for (String f : allowedFields) {
            if (f.equalsIgnoreCase(field)) {
                return userDAO.updateFieldById(id, field, value);
            }
        }
        return false;
    }

    public boolean deleteUserById(int id) {
        return userDAO.deleteUser(id);
    }
}
