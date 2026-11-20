package com.marcel.medical_01.exception;

import com.marcel.medical_01.controller.PatientController;
import com.marcel.medical_01.dto.doctor.DoctorUpdateRequest;
import com.marcel.medical_01.mapper.PatientMapper;
import com.marcel.medical_01.service.PatientService;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.marcel.medical_01.dto.patient.PatientUpdateRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.marcel.medical_01.controller.DoctorController;
import com.marcel.medical_01.service.DoctorService;
import com.marcel.medical_01.dto.doctor.DoctorUpdateRequest;

class GlobalExceptionHandlerTest {

    @Test
    void shouldReturnNotFoundWhenPatientDoesNotExist() throws Exception {

        // Arrange
        Long patientId = 999L;

        PatientService patientService = mock(PatientService.class);
        PatientMapper patientMapper = mock(PatientMapper.class);

        PatientController patientController =
                new PatientController(patientService, patientMapper);

        doThrow(new PatientNotFoundException())
                .when(patientService)
                .delete(patientId);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(patientController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        // Act & Assert
        mockMvc.perform(delete("/api/patients/{id}", patientId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingMissingPatient() throws Exception {

        // Arrange
        Long patientId = 999L;

        PatientService patientService = mock(PatientService.class);
        PatientMapper patientMapper = mock(PatientMapper.class);

        PatientController patientController =
                new PatientController(patientService, patientMapper);

        when(patientService.update(
                eq(patientId),
                any(PatientUpdateRequest.class)
        )).thenThrow(new PatientNotFoundException());

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(patientController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        // Act & Assert
        mockMvc.perform(
                put("/api/patients/{id}", patientId)
                        .contentType("application/json")
                        .content("""
                                {
                                    "cpf": "12345678901",
                                    "birthDate": "1990-05-15",
                                    "phone": "11999999999",
                                    "paymentType": "PRIVATE",
                                    "healthPlanId": null
                                }
                                """)
        ).andExpect(status().isNotFound());

        verify(patientService)
                .update(eq(patientId), any(PatientUpdateRequest.class));

        verifyNoInteractions(patientMapper);
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingMissingDoctor() throws Exception {

        // Arrange
        Long doctorId = 999L;

        DoctorService doctorService = mock(DoctorService.class);

        DoctorController doctorController =
                new DoctorController(doctorService);

        when(doctorService.update(
                eq(doctorId),
                any(DoctorUpdateRequest.class)
        )).thenThrow(new DoctorNotFoundException());

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(doctorController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        // Act & Assert
        mockMvc.perform(
                put("/api/doctors/{id}", doctorId)
                        .contentType("application/json")
                        .content("""
                            {
                                "crm": "987654",
                                "crmUf": "RJ"
                            }
                            """)
        ).andExpect(status().isNotFound());

        // Verify
        verify(doctorService).update(
                eq(doctorId),
                any(DoctorUpdateRequest.class)
        );

        System.out.println(
                "[GlobalExceptionHandlerTest] HTTP 404 => Doctor not found"
        );
    }
}
