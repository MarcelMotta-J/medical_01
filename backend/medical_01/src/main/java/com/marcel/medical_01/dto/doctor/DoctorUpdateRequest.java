package com.marcel.medical_01.dto.doctor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DoctorUpdateRequest(

        @NotBlank
        @Size(max = 20)
        String crm,

        @NotBlank
        @Pattern(regexp = "[A-Z]{2}")
        String crmUf

) {}
