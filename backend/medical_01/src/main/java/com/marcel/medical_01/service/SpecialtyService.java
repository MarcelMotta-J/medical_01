
package com.marcel.medical_01.service;

import com.marcel.medical_01.model.Specialty;
import com.marcel.medical_01.repository.SpecialtyRepository;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    public SpecialtyService(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }


    public Specialty save(Specialty specialty) {

        if (specialtyRepository.existsByName(specialty.getName())) {
            throw new IllegalArgumentException(
                    "Specialty already exists."
            );
        }

        return specialtyRepository.save(specialty);
    }


    public Optional<Specialty> findById(Long id) {
        return specialtyRepository.findById(id);
    }
}