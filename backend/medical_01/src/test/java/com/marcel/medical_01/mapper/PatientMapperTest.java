package com.marcel.medical_01.mapper;

import com.marcel.medical_01.dto.patient.PatientRequest;
import com.marcel.medical_01.model.HealthPlan;
import com.marcel.medical_01.model.Patient;
import com.marcel.medical_01.model.PaymentType;
import com.marcel.medical_01.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import static org.junit.jupiter.api.Assertions.assertNull;

import com.marcel.medical_01.dto.patient.PatientResponse;

class PatientMapperTest {

    //PatientRequest + User + HealthPlan ->  PatientMapper ->  Patient
    @Test
    void shouldMapHealthInsurancePatientRequestToEntity() {

        PatientMapper patientMapper =
                new PatientMapper();

        PatientRequest request =
                new PatientRequest(
                        1L,
                        "12345678901",
                        LocalDate.of(1985, 7, 3),
                        "11999999999",
                        PaymentType.HEALTH_INSURANCE,
                        10L
                );

        User user = new User();
        user.setId(1L);

        HealthPlan healthPlan = new HealthPlan();
        healthPlan.setId(10L);
        healthPlan.setName("Unimed");
        healthPlan.setActive(true);

        Patient patient =
                patientMapper.toEntity(
                        request,
                        user,
                        healthPlan
                );

        assertSame(user, patient.getUser());
        assertSame(healthPlan, patient.getHealthPlan());

        assertEquals(
                "12345678901",
                patient.getCpf()
        );

        assertEquals(
                LocalDate.of(1985, 7, 3),
                patient.getBirthDate()
        );

        assertEquals(
                "11999999999",
                patient.getPhone()
        );

        assertEquals(
                PaymentType.HEALTH_INSURANCE,
                patient.getPaymentType()
        );
    }

    //PaymentType.PRIVATE  ->  healthPlan = null
    @Test
    void shouldMapPrivatePatientRequestToEntityWithoutHealthPlan() {

        PatientMapper patientMapper =
                new PatientMapper();

        PatientRequest request =
                new PatientRequest(
                        1L,
                        "12345678901",
                        LocalDate.of(1985, 7, 3),
                        "11999999999",
                        PaymentType.PRIVATE,
                        null
                );

        User user = new User();
        user.setId(1L);

        Patient patient =
                patientMapper.toEntity(
                        request,
                        user,
                        null
                );

        assertSame(user, patient.getUser());

        assertEquals(
                PaymentType.PRIVATE,
                patient.getPaymentType()
        );

        assertNull(patient.getHealthPlan());
    }

    @Test
    void shouldMapHealthInsurancePatientToResponse() {

        PatientMapper patientMapper =
                new PatientMapper();

        User user = new User();
        user.setId(1L);
        user.setName("Marty McFly");

        HealthPlan healthPlan = new HealthPlan();
        healthPlan.setId(10L);
        healthPlan.setName("Unimed");
        healthPlan.setActive(true);

        Patient patient = new Patient();
        patient.setId(100L);
        patient.setUser(user);
        patient.setCpf("12345678901");
        patient.setBirthDate(
                LocalDate.of(1985, 7, 3)
        );
        patient.setPhone("11999999999");
        patient.setPaymentType(
                PaymentType.HEALTH_INSURANCE
        );
        patient.setHealthPlan(healthPlan);

        PatientResponse response =
                patientMapper.toResponse(patient);

        assertEquals(100L, response.id());
        assertEquals(1L, response.userId());
        assertEquals("Marty McFly", response.userName());
        assertEquals("12345678901", response.cpf());

        assertEquals(
                LocalDate.of(1985, 7, 3),
                response.birthDate()
        );

        assertEquals(
                "11999999999",
                response.phone()
        );

        assertEquals(
                PaymentType.HEALTH_INSURANCE,
                response.paymentType()
        );

        assertEquals(10L, response.healthPlanId());
        assertEquals("Unimed", response.healthPlanName());
    }

    @Test
    void shouldMapPrivatePatientToResponseWithoutHealthPlan() {

        PatientMapper patientMapper =
                new PatientMapper();

        User user = new User();
        user.setId(1L);
        user.setName("Marty McFly");

        Patient patient = new Patient();
        patient.setId(100L);
        patient.setUser(user);
        patient.setCpf("12345678901");
        patient.setBirthDate(
                LocalDate.of(1985, 7, 3)
        );
        patient.setPhone("11999999999");
        patient.setPaymentType(
                PaymentType.PRIVATE
        );
        patient.setHealthPlan(null);

        PatientResponse response =
                patientMapper.toResponse(patient);

        assertEquals(
                PaymentType.PRIVATE,
                response.paymentType()
        );

        assertNull(response.healthPlanId());
        assertNull(response.healthPlanName());
    }
}
