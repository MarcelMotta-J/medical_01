package com.marcel.medical_01.mapper;

import com.marcel.medical_01.dto.patient.PatientRequest;
import com.marcel.medical_01.dto.patient.PatientResponse;
import com.marcel.medical_01.model.HealthPlan;
import com.marcel.medical_01.model.Patient;
import com.marcel.medical_01.model.User;

import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public PatientResponse toResponse(Patient patient) {

        return new PatientResponse(
                patient.getId(),
                patient.getUser().getId(),
                patient.getUser().getName(),
                patient.getCpf(),
                patient.getBirthDate(),
                patient.getPhone(),
                patient.getPaymentType(),
                patient.getHealthPlan() != null
                        ? patient.getHealthPlan().getId()
                        : null,
                patient.getHealthPlan() != null
                        ? patient.getHealthPlan().getName()
                        : null
        );
    }

    public Patient toEntity(
            PatientRequest request,
            User user,
            HealthPlan healthPlan
    ) {

        Patient patient = new Patient();

        patient.setUser(user);
        patient.setCpf(request.cpf());
        patient.setBirthDate(request.birthDate());
        patient.setPhone(request.phone());
        patient.setPaymentType(request.paymentType());
        patient.setHealthPlan(healthPlan);

        return patient;
    }


}
