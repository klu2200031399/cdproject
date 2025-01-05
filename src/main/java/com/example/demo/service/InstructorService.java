package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Instructor;
import com.example.demo.reposetory.InstructorRepository;

@Service
public class InstructorService {
    @Autowired
    private InstructorRepository instructorRepository;

    public void saveInstructor(Instructor instructor) {
        instructorRepository.save(instructor); // This saves an Instructor object
    }
    
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll(); // Fetch all instructors from the database
    }
}
