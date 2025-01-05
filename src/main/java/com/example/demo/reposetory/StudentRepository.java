package com.example.demo.reposetory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByUsernameAndPassword(Long username, String password);
    Optional<Student> findByUsername(Long username);
    Optional<Student> findById(Long id);
}
