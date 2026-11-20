package com.marcel.medical_01.dto.patient;

import com.marcel.medical_01.model.PaymentType;

import java.time.LocalDate;

public record PatientResponse(
        Long id,
        Long userId,
        String userName,
        String cpf,
        LocalDate birthDate,
        String phone,
        PaymentType paymentType,
        Long healthPlanId,
        String healthPlanName
) {
}
