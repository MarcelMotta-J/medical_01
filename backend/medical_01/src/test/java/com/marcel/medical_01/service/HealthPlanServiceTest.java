package com.marcel.medical_01.service;

import com.marcel.medical_01.model.HealthPlan;
import com.marcel.medical_01.repository.HealthPlanRepository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class HealthPlanServiceTest {

    //nome NÃO existe  -> deve salvar HealthPlan ✅
    @Test
    void shouldSaveHealthPlanWhenNameDoesNotExist() {

        HealthPlanRepository healthPlanRepository =
                mock(HealthPlanRepository.class);

        HealthPlanService healthPlanService =
                new HealthPlanService(healthPlanRepository);

        HealthPlan healthPlan = new HealthPlan();
        healthPlan.setName("Unimed");
        healthPlan.setActive(true);

        when(healthPlanRepository.existsByName("Unimed"))
                .thenReturn(false);

        when(healthPlanRepository.save(healthPlan))
                .thenReturn(healthPlan);

        HealthPlan savedHealthPlan =
                healthPlanService.save(healthPlan);

        assertSame(healthPlan, savedHealthPlan);
    }

    // IllegalArgumentException ✅ -> repository.save() NÃO PODE SER CHAMADO
    @Test
    void shouldRejectHealthPlanWhenNameAlreadyExists() {

        HealthPlanRepository healthPlanRepository =
                mock(HealthPlanRepository.class);

        HealthPlanService healthPlanService =
                new HealthPlanService(healthPlanRepository);

        HealthPlan healthPlan = new HealthPlan();
        healthPlan.setName("Unimed");
        healthPlan.setActive(true);

        when(healthPlanRepository.existsByName("Unimed"))
                .thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> healthPlanService.save(healthPlan)
        );

        verify(healthPlanRepository, never())
                .save(healthPlan);
    }
}
