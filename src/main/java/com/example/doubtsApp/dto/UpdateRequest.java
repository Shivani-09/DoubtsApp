package com.example.doubtsApp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequest { // Name it UpdateRequest or UserUpdateRequest

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be a valid email address")
    private String email;

    // Make password optional for updates.
    // Remove @NotBlank. You can keep @Size if you want to validate only if it's provided.
    @Size(min = 6, message = "Password must be at least 6 characters long if provided")
    private String password; // No @NotBlank here
}