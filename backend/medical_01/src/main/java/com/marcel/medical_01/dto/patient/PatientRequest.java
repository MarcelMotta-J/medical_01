package com.marcel.medical_01.dto.patient;

import com.marcel.medical_01.model.PaymentType;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record PatientRequest(
        @NotNull
        Long userId,

        @NotBlank
        String cpf,

        @NotNull
        @Past
        LocalDate birthDate,

        @NotBlank
        String phone,

        @NotNull
        PaymentType paymentType,

        Long healthPlanId
) {
}
