package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Admin;
import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import com.example.demo.service.AdminService;
import com.example.demo.service.StudentService;
import com.example.demo.service.UserService;

@Controller
public class LoginController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService; // Injecting the UserService to update user data on login
    
    @GetMapping("/")
    public String HomePage() {
        return "home"; // Make sure this points to your Thymeleaf template (src/main/resources/templates/login.html)
    }
    // Show the login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Make sure this points to your Thymeleaf template (src/main/resources/templates/login.html)
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username, 
                               @RequestParam("password") String password, 
                               Model model) {
        
        // Check if the username is for a student (10 digits) or admin (4 digits)
        if (username.length() == 10) {
            Optional<Student> student = studentService.login(Long.parseLong(username), password);
            if (student.isPresent()) {
                // Update user data on login
                userService.updateUserOnLogin(username, student.get().getName(), "student");
                return "redirect:/student-dashboard"; // Redirect to student dashboard
            }
        } else if (username.length() == 4) {
            Optional<Admin> admin = adminService.login(Integer.parseInt(username), password);
            if (admin.isPresent()) {
                // Update user data on login
                userService.updateUserOnLogin(username, admin.get().getName(), "admin");
                return "redirect:/admin-dashboard"; // Redirect to admin dashboard
            }
        }
        
        // If login failed
        model.addAttribute("error", "Invalid username or password");
        return "login"; // Return to login page if credentials are invalid
    }

    // View the profile of the logged-in user
    @GetMapping("/view-profile")
    public String viewProfile(@RequestParam String userId, Model model) {
        // Retrieve user details by userId
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "profile"; // Return to Thymeleaf profile template (src/main/resources/templates/profile.html)
    }
}
