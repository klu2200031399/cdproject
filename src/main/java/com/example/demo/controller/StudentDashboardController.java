package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Course;
import com.example.demo.entity.Feedback;
import com.example.demo.entity.InstitutionalService;
import com.example.demo.entity.Instructor;
import com.example.demo.entity.User;
import com.example.demo.reposetory.CourseRepository;
import com.example.demo.reposetory.FeedbackRepository;
import com.example.demo.reposetory.InstitutionalServiceRepository;
import com.example.demo.reposetory.InstructorRepository;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/student-dashboard")
public class StudentDashboardController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private InstitutionalServiceRepository institutionalServiceRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserService userService;

    // Show student dashboard with the last logged-in user
    @GetMapping
    public String showStudentDashboard(Model model) {
        User user = userService.getLastLoggedInUser();  // Get the current logged-in user
        model.addAttribute("user", user);
        return "student-dashboard";
    }

    // Show the feedback form and populate courses, instructors, and services
    @GetMapping("/student/give-feedback")
    public String showGiveFeedbackForm(Model model) {
        // Retrieve the list of courses, instructors, and services
        List<Course> courses = courseRepository.findAll();
        List<Instructor> instructors = instructorRepository.findAll();
        List<InstitutionalService> services = institutionalServiceRepository.findAll();

        // Get the current logged-in user and pass it to the form
        User user = userService.getLastLoggedInUser();
        
        // Add attributes to the model
        model.addAttribute("user", user);
        model.addAttribute("courses", courses);
        model.addAttribute("instructors", instructors);
        model.addAttribute("services", services);
        model.addAttribute("feedback", new Feedback());

        return "student/give-feedback"; // Refers to the name of your Thymeleaf template
    }

    // Submit feedback form with the necessary details
    @PostMapping("/student/give-feedback")
    public String submitFeedback(@RequestParam("category") String category,
                                 @RequestParam(value = "course", required = false) String courseName,
                                 @RequestParam(value = "instructor", required = false) String instructorName,
                                 @RequestParam(value = "service", required = false) String serviceName,
                                 @RequestParam("feedback") String feedbackText,
                                 @RequestParam("rating") Integer rating,
                                 Model model) { // New parameter for rating

        // Get the current logged-in user
        User user = userService.getLastLoggedInUser();
        
        if (user == null) {
            model.addAttribute("error", "User not found");
            return "error"; // Handle error appropriately
        }

        Feedback feedback = new Feedback();
        feedback.setCategory(category);

        // Set the feedback details based on category
        if ("course".equals(category)) {
            feedback.setCourseName(courseName);
        } else if ("instructor".equals(category)) {
            feedback.setInstructorName(instructorName);
        } else if ("service".equals(category)) {
            feedback.setServiceName(serviceName);
        }

        feedback.setFeedback(feedbackText);
        feedback.setUsername(user.getUserId()); // Set the logged-in user's userId
        feedback.setRating(rating); // Set the rating

        // Save feedback to the database
        feedbackRepository.save(feedback);

        return "redirect:/student-dashboard/student/give-feedback"; // Redirect to the feedback form page
    }

    // Show the profile of the last logged-in user
    @GetMapping("/student/profile")
    public String getLastLoggedInUserProfile(Model model) {
        // Fetch the last logged-in user
        User user = userService.getLastLoggedInUser();  // Calling the service method to get the last logged-in user
        
        if (user != null) {
            // Add user details to the model
            model.addAttribute("user", user);
        } else {
            model.addAttribute("error", "No users found");
        }

        return "student/profile"; // Thymeleaf template to show the user profile
    }
    
    // View all feedback submitted by the student
    @GetMapping("/student/view-feedback")
    public String viewFeedback(Model model) {
        User user = userService.getLastLoggedInUser();
        List<Feedback> feedbackList = feedbackRepository.findByUsername(user.getUserId());
        model.addAttribute("feedbackList", feedbackList);

        return "student/view-feedback"; 
    }

    // Delete a feedback entry
    @GetMapping("/student/delete-feedback")
    public String deleteFeedback(@RequestParam("feedbackId") Long feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId).orElse(null);

        if (feedback != null) {
            feedbackRepository.delete(feedback);  // Delete the feedback
        }

        return "redirect:/student-dashboard/student/view-feedback"; // Redirect back to view-feedback page
    }
}
