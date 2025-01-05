package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.InstitutionalService;
import com.example.demo.reposetory.InstitutionalServiceRepository;

@Service
public class InstitutionalServiceService {
    @Autowired
    private InstitutionalServiceRepository institutionalServiceRepository;

    public void saveService(InstitutionalService service) {
        institutionalServiceRepository.save(service);
    }
    public List<InstitutionalService> getAllServices() {
        return institutionalServiceRepository.findAll(); // Retrieves all services
    }
}
