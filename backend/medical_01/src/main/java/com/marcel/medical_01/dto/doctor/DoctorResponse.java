package com.marcel.medical_01.dto.doctor;

public record DoctorResponse(

        Long id,

        Long userId,

        String name,

        String crm,

        String crmUf,

        boolean active

) {
}
