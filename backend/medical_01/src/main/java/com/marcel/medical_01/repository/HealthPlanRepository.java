package com.marcel.medical_01.repository;

import com.marcel.medical_01.model.HealthPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthPlanRepository
        extends JpaRepository<HealthPlan, Long> {

        boolean existsByName(String name);
}
