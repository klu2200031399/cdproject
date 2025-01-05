package com.example.demo.reposetory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Instructor;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
}
