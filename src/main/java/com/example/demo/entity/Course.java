package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    private String courseName;

    private String courseCode;  // Replaced passOutYear with courseCode of type String

    // Default constructor (required by JPA)
    public Course() {}

    // Parameterized constructor for convenience
    public Course(String courseName, String courseCode) {
        this.courseName = courseName;
        this.courseCode = courseCode;
    }

    // Getters and Setters
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    // Optional: toString method for easier debugging/logging
    @Override
    public String toString() {
        return "Course{" +
               "courseId=" + courseId +
               ", courseName='" + courseName + '\'' +
               ", courseCode='" + courseCode + '\'' +
               '}';
    }
}
