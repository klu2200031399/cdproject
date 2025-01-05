package com.example.demo.reposetory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Feedback;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByUsername(String username);
}
