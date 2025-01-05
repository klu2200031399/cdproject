package com.example.demo.reposetory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
	Optional<Course> findByCourseCode(String courseCode);
}
