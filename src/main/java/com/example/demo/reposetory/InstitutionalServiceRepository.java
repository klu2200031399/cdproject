package com.example.demo.reposetory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.InstitutionalService;

public interface InstitutionalServiceRepository extends JpaRepository<InstitutionalService, Long> {
}
