package com.example.demo.service;

import com.example.demo.entity.Course;
import com.example.demo.reposetory.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public void saveCourse(Course course) {
        courseRepository.save(course); // Save to the database
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll(); // Retrieve all courses
    }

    @Override
    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                               .orElseThrow(() -> new RuntimeException("Course not found for id: " + courseId));
    }

    @Override
    public Course findCourseByCode(String courseCode) {
        return courseRepository.findByCourseCode(courseCode)
                               .orElseThrow(() -> new RuntimeException("Course not found for code: " + courseCode));
    }

    @Override
    public void updateCourse(Course course) {
        Course existingCourse = findCourseByCode(course.getCourseCode());
        existingCourse.setCourseName(course.getCourseName());
        // Update any other necessary fields here
        courseRepository.save(existingCourse); // Save the updated course
    }

    @Override
    public void deleteCourseByCode(String courseCode) {
        Course course = findCourseByCode(courseCode);
        courseRepository.delete(course); // Delete the course
    }
}