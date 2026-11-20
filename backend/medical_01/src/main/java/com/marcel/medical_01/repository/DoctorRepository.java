package com.marcel.medical_01.repository;

import com.marcel.medical_01.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
