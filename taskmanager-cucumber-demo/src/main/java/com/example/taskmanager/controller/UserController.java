package com.example.taskmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.taskmanager.model.User;
import com.example.taskmanager.repo.UserRepository;

import jakarta.validation.Valid;

@Controller
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model) {
        // Custom validation for password confirmation
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.user", "Passwords do not match");
        }

        // Check for existing email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "error.user", "Email already registered");
        }

        if (bindingResult.hasErrors()) {
            return "signup";
        }

        // Save the user
        userRepository.save(user);
        return "redirect:/signin?registered";
    }

    @GetMapping("/signin")
    public String showSigninForm(
            @RequestParam(required = false) String registered,
            @RequestParam(required = false) String signedout,
            Model model) {
        if (registered != null) {
            model.addAttribute("message", "Registration successful! Please sign in.");
        }
        if (signedout != null) {
            model.addAttribute("message", "You have been successfully signed out.");
        }
        return "signin";
    }

    @PostMapping("/signin")
    public String signin(@RequestParam String email, @RequestParam String password, Model model) {
        // Check if email is empty
        if (email == null || email.trim().isEmpty()) {
            model.addAttribute("error", "Email is required");
            return "signin";
        }

        // Check if password is empty
        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "Password is required");
            model.addAttribute("email", email);
            return "signin";
        }

        // Check password length
        if (password.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters long");
            model.addAttribute("email", email);
            model.addAttribute("passwordError", "Password is too short (minimum 6 characters required)");
            return "signin";
        }

        // Check if email format is valid
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            model.addAttribute("error", "Please enter a valid email address");
            model.addAttribute("emailError", "Invalid email format");
            return "signin";
        }

        // Check if user exists
        var userOpt = userRepository.findByEmail(email);
        if (!userOpt.isPresent()) {
            model.addAttribute("error", "No account found with this email address");
            model.addAttribute("email", email);
            return "signin";
        }

        // Check password
        if (!userOpt.get().getPassword().equals(password)) {
            model.addAttribute("error", "Invalid email or password");
            model.addAttribute("email", email);
            model.addAttribute("passwordError", "Invalid email or password");
            return "signin";
        }

        return "redirect:/tasks";
    }

    @GetMapping("/signin-success")
    public String signinSuccess() {
        return "signin-success";
    }

    @PostMapping("/signout")
    public String signout() {
        return "signout";
    }
}
