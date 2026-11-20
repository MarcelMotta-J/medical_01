package com.marcel.medical_01.service;

import com.marcel.medical_01.model.HealthPlan;
import com.marcel.medical_01.repository.HealthPlanRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HealthPlanService {

    private final HealthPlanRepository healthPlanRepository;

    public HealthPlanService(HealthPlanRepository healthPlanRepository) {
        this.healthPlanRepository = healthPlanRepository;
    }

    public HealthPlan save(HealthPlan healthPlan) {

        if (healthPlanRepository.existsByName(healthPlan.getName())) {
            throw new IllegalArgumentException(
                    "Health plan already exists."
            );
        }

        return healthPlanRepository.save(healthPlan);
    }

    public Optional<HealthPlan> findById(Long id) {
        return healthPlanRepository.findById(id);
    }
}
