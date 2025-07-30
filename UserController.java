package com.example.updatesql.controller;

import com.example.updatesql.model.UpdateRequest;
import com.example.updatesql.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "update";
    }

    @PostMapping("/update")
    @ResponseBody
    public String updateUser(@ModelAttribute UpdateRequest request) {
        if (request.getField() == null || request.getField().isEmpty() ||
            request.getValue() == null || request.getValue().isEmpty()) {
            return "❌ Field and Value must not be empty.";
        }

        boolean updated = userService.updateUserField(request.getId(), request.getField(), request.getValue());
        return updated ? "✅ Updated successfully." : "❌ Invalid ID or field.";
    }

    @PostMapping("/delete")
    @ResponseBody
    public String deleteUser(@RequestParam int id) {
        boolean deleted = userService.deleteUserById(id);
        return deleted ? "✅ Deleted successfully." : "❌ User not found.";
    }
}
