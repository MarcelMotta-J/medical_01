package com.marcel.medical_01.dto.patient;

import com.marcel.medical_01.model.PaymentType;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Past;

import jakarta.validation.constraints.NotNull;


public record PatientUpdateRequest(
        @NotBlank String cpf,
        @NotNull @Past LocalDate birthDate,
        @NotBlank String phone,
        @NotNull PaymentType paymentType,
        Long healthPlanId
) {
}
