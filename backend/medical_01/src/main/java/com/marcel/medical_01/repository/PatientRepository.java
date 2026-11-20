package com.marcel.medical_01.repository;

import com.marcel.medical_01.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
