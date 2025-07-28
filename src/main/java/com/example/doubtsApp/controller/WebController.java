package com.example.doubtsApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    // Serves the login.html page when navigating to /login-page
    @GetMapping("/login-page")
    public String loginPage() {
        return "login"; // Refers to src/main/resources/templates/login.html
    }

    // Serves the register.html page when navigating to /register-page
    @GetMapping("/register-page")
    public String registerPage() {
        return "register"; // Refers to src/main/resources/templates/register.html
    }

    // Serves a simple dashboard.html page after successful login
    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "dashboard"; // Refers to src/main/resources/templates/dashboard.html
    }

    // Optional: Redirect root to login page
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login-page";
    }
}