package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Admin;
import com.example.demo.entity.Student;
import com.example.demo.service.AdminService;
import com.example.demo.service.StudentService;

@Controller
public class RegistrationController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private AdminService adminService;

    // Show the student registration page
    @GetMapping("/student/register")
    public String showStudentRegistrationPage() {
        return "student-register";
    }

    // Handle student registration
    @PostMapping("/student/register")
    public String registerStudent(@ModelAttribute Student student, Model model) {
        // Check if the student username already exists
        if (studentService.isUsernameExists(student.getUsername())) {
            model.addAttribute("error", "Username already exists. Please choose a different one.");
            return "student-register"; // Return the registration page with an error message
        }
        
        studentService.registerStudent(student);
        return "redirect:/login";
    }

    // Show the admin registration page
    @GetMapping("/admin/register")
    public String showAdminRegistrationPage() {
        return "admin-register";
    }

    // Handle admin registration
    @PostMapping("/admin/register")
    public String registerAdmin(@ModelAttribute Admin admin, Model model) {
        // Check if the admin username already exists
        if (adminService.isUsernameExists(admin.getUsername())) {
            model.addAttribute("error", "Username already exists. Please choose a different one.");
            return "admin-register"; // Return the registration page with an error message
        }
        
        adminService.registerAdmin(admin);
        return "redirect:/login";
    }
}
