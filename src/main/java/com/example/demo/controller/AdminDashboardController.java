package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Course;
import com.example.demo.entity.Feedback;
import com.example.demo.entity.InstitutionalService;
import com.example.demo.entity.Instructor;
import com.example.demo.entity.User;
import com.example.demo.service.AdminService;
import com.example.demo.service.CourseService;
import com.example.demo.service.FeedbackService;
import com.example.demo.service.InstitutionalServiceService;
import com.example.demo.service.InstructorService;
import com.example.demo.service.UserService;

import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping("/admin-dashboard")
public class AdminDashboardController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private InstructorService instructorService;
    @Autowired
    private InstitutionalServiceService institutionalServiceService;
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;

    // Show Admin Dashboard
    @GetMapping
    public String showAdminDashboard(Model model) {
    	User user = userService.getLastLoggedInUser();  // Get the current logged-in user
        model.addAttribute("user", user);
        return "admin-dashboard"; // Main dashboard view for admins
    }

    // Show Add Course Form
    @GetMapping("/admin/add-course")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("courses", courseService.getAllCourses()); // Add courses to the model
        return "admin/add-course"; // Return the view to add a course
    }

    // Add a Course
    @PostMapping("/admin/add-course")
    public String addCourse(@ModelAttribute Course course) {
        courseService.saveCourse(course); // Save the course
        return "redirect:/admin-dashboard/admin/add-course"; // Redirect back to the add-course page
    }

    // Show Add Instructor Form
    @GetMapping("/admin/add-instructor")
    public String showAddInstructorForm(Model model) {
        // Add an empty Instructor object for the form
        model.addAttribute("instructor", new Instructor());
        
        // Add the list of existing instructors to display in the table
        model.addAttribute("instructors", instructorService.getAllInstructors());
        
        return "admin/add-instructor"; // Return the view to add an instructor
    }

    // Add an Instructor
    @PostMapping("/admin/add-instructor")
    public String addInstructor(@ModelAttribute Instructor instructor) {
        instructorService.saveInstructor(instructor); // Save the instructor
        return "redirect:/admin-dashboard/admin/add-instructor"; // Redirect back to the add-instructor page
    }


    // Show Add Service Form
    @GetMapping("/admin/add-service")
    public String showAddServiceForm(Model model) {
        // Add an empty InstitutionalService object for the form
        model.addAttribute("service", new InstitutionalService());
        
        // Add the list of existing services to display in the table
        model.addAttribute("services", institutionalServiceService.getAllServices());
        
        return "admin/add-service"; // Return the view to add a service
    }

    // Add a new Institutional Service
    @PostMapping("/admin/add-service")
    public String addService(@ModelAttribute InstitutionalService service) {
        institutionalServiceService.saveService(service); // Save the new service
        return "redirect:/admin-dashboard/admin/add-service"; // Redirect back to the add-service page
    }


    // View Feedbacks
    @GetMapping("/admin/view-feedback")
    public String viewFeedbacks(Model model) {
        // Example list of feedbacks fetched from the database
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();

        // Group feedback by category and name, count occurrences
        Map<String, Long> courseRatings = feedbacks.stream()
            .filter(feedback -> "course".equals(feedback.getCategory()))
            .collect(Collectors.groupingBy(Feedback::getCourseName, Collectors.counting()));

        Map<String, Long> instructorRatings = feedbacks.stream()
            .filter(feedback -> "instructor".equals(feedback.getCategory()))
            .collect(Collectors.groupingBy(Feedback::getInstructorName, Collectors.counting()));

        Map<String, Long> serviceRatings = feedbacks.stream()
            .filter(feedback -> "service".equals(feedback.getCategory()))
            .collect(Collectors.groupingBy(Feedback::getServiceName, Collectors.counting()));

        // Find the highest-rated name in each category
        String highestRatedCourse = courseRatings.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey).orElse("N/A");

        String highestRatedInstructor = instructorRatings.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey).orElse("N/A");

        String highestRatedService = serviceRatings.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey).orElse("N/A");

        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("highestRatedCourse", highestRatedCourse);
        model.addAttribute("highestRatedInstructor", highestRatedInstructor);
        model.addAttribute("highestRatedService", highestRatedService);

        return "admin/view-feedback"; // Thymeleaf template name
    }

    // Delete Feedback
    @GetMapping("/admin/delete-feedback")
    public String deleteFeedback(@RequestParam("feedbackId") Long feedbackId) {
        feedbackService.deleteFeedback(feedbackId); // Call the service to delete the feedback
        return "redirect:/admin-dashboard/admin/view-feedback"; // Redirect to the view feedback page
    }

    // View User Profile
    @GetMapping("/admin/profile")
    public String getLastLoggedInUserProfile(Model model) {
        // Fetch the last logged-in user from the database
        User user = userService.getLastLoggedInUser();  // Calling the service method to get the last logged-in user
        
        if (user != null) {
            // Add user details to the model
            model.addAttribute("user", user);
        } else {
            model.addAttribute("error", "No users found");
        }

        return "admin/profile"; // Thymeleaf template to show the user profile
    }
    @GetMapping("/admin/edit-course")
    public String showEditCourseForm(@RequestParam("courseCode") String courseCode, Model model) {
        Course course = courseService.findCourseByCode(courseCode); // Fetch course details
        model.addAttribute("course", course); // Add course to the model
        return "edit-course-form"; // Return the name of the Thymeleaf template for editing
    }

    // Update Course
    @PostMapping("/admin/edit-course")
    public String updateCourse(@ModelAttribute Course course, RedirectAttributes redirectAttributes) {
        courseService.updateCourse(course); // Update the course in the database
        redirectAttributes.addFlashAttribute("success", "Course updated successfully!");
        return "redirect:/admin-dashboard/admin/add-course"; // Redirect to the course list
    }

    // Delete Course
    @PostMapping("/admin/delete-course")
    public String deleteCourse(@RequestParam("courseCode") String courseCode, RedirectAttributes redirectAttributes) {
        courseService.deleteCourseByCode(courseCode); // Delete the course from the database
        redirectAttributes.addFlashAttribute("success", "Course deleted successfully!");
        return "redirect:/admin-dashboard/admin/add-course";
    }

}
