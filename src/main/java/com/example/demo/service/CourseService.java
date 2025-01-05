package com.example.demo.service;

import java.util.List;
import com.example.demo.entity.Course;

public interface CourseService {
    void saveCourse(Course course);
    List<Course> getAllCourses();
    Course getCourseById(Long courseId);
    Course findCourseByCode(String courseCode);
    void updateCourse(Course course);
    void deleteCourseByCode(String courseCode);
}