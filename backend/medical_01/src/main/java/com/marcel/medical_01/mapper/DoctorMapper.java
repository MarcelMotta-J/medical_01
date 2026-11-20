package com.marcel.medical_01.mapper;

import com.marcel.medical_01.model.Doctor;
import com.marcel.medical_01.dto.doctor.DoctorResponse;

public class DoctorMapper {

    public static DoctorResponse toResponse(Doctor doctor) {

        return new DoctorResponse(
                doctor.getId(),
                doctor.getUser().getId(),
                doctor.getUser().getName(),
                doctor.getCrm(),
                doctor.getCrmUf(),
                doctor.isActive()
        );
    }
}
