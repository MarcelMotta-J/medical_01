
package com.marcel.medical_01.repository;

import com.marcel.medical_01.model.Specialty;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialtyRepository
        extends JpaRepository<Specialty, Long> {
}
