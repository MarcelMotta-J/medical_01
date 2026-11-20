package com.marcel.medical_01.mapper;

import com.marcel.medical_01.dto.doctor.DoctorResponse;
import com.marcel.medical_01.model.Doctor;
import com.marcel.medical_01.model.User;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DoctorMapperTest {

    @Test
    void shouldConvertDoctorToDoctorResponse() {

        // 1. Create a user
        User user = new User();
        user.setId(10L);
        user.setName("Dr. Emmett Brown");

        // 2. Create a doctor
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setUser(user);
        doctor.setCrm("123456");
        doctor.setCrmUf("SP");

        // 3. Convert Entity to DTO
        DoctorResponse response = DoctorMapper.toResponse(doctor);

        // 4. Verify converted data
        assertEquals(1L, response.id());
        assertEquals(10L, response.userId());
        assertEquals("Dr. Emmett Brown", response.name());
        assertEquals("123456", response.crm());
        assertEquals("SP", response.crmUf());

        // 5. Print result
        System.out.println(
                "[DoctorMapperTest] Doctor converted => "
                        + response.name()
                        + " | CRM: " + response.crm()
                        + "/" + response.crmUf()
        );
    }
}
