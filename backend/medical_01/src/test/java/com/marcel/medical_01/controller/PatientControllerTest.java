package com.marcel.medical_01.controller;

import com.marcel.medical_01.dto.patient.PatientRequest;
import com.marcel.medical_01.dto.patient.PatientResponse;
import com.marcel.medical_01.exception.GlobalExceptionHandler;
import com.marcel.medical_01.mapper.PatientMapper;
import com.marcel.medical_01.model.Patient;
import com.marcel.medical_01.model.PaymentType;
import com.marcel.medical_01.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import java.util.Optional;

import com.marcel.medical_01.dto.patient.PatientUpdateRequest;

import org.springframework.http.HttpStatus;

import com.marcel.medical_01.exception.PatientNotFoundException;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verifyNoInteractions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

class PatientControllerTest {

    @Test
    void shouldCreatePatient() {

        PatientService patientService =
                mock(PatientService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientController patientController =
                new PatientController(
                        patientService,
                        patientMapper
                );

        PatientRequest request =
                new PatientRequest(
                        1L,
                        "12345678901",
                        LocalDate.of(1985, 7, 3),
                        "11999999999",
                        PaymentType.PRIVATE,
                        null
                );

        Patient patient =
                new Patient();

        PatientResponse response =
                new PatientResponse(
                        100L,
                        1L,
                        "Marty McFly",
                        "12345678901",
                        LocalDate.of(1985, 7, 3),
                        "11999999999",
                        PaymentType.PRIVATE,
                        null,
                        null
                );

        when(patientService.create(request))
                .thenReturn(patient);

        when(patientMapper.toResponse(patient))
                .thenReturn(response);

        ResponseEntity<PatientResponse> result =
                patientController.create(request);

        assertSame(
                response,
                result.getBody()
        );

        assertEquals(
                201,
                result.getStatusCode().value()
        );

        verify(patientService)
                .create(request);

        verify(patientMapper)
                .toResponse(patient);
    }

    @Test
    void shouldFindAllPatients() {

        // Arrange
        PatientService patientService =
                mock(PatientService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientController patientController =
                new PatientController(
                        patientService,
                        patientMapper
                );

        Patient patient = new Patient();

        PatientResponse response =
                new PatientResponse(
                        100L,
                        1L,
                        "Marty McFly",
                        "12345678901",
                        LocalDate.of(1985, 7, 3),
                        "11999999999",
                        PaymentType.PRIVATE,
                        null,
                        null
                );

        when(patientService.findAll())
                .thenReturn(List.of(patient));

        when(patientMapper.toResponse(patient))
                .thenReturn(response);

        // Act
        ResponseEntity<List<PatientResponse>> result =
                patientController.findAll();

        // Assert
        assertEquals(200, result.getStatusCode().value());

        assertEquals(
                List.of(response),
                result.getBody()
        );

        verify(patientService).findAll();
        verify(patientMapper).toResponse(patient);
    }

    @Test
    void shouldFindPatientById() {

        // Arrange
        Long patientId = 100L;

        PatientService patientService =
                mock(PatientService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientController patientController =
                new PatientController(
                        patientService,
                        patientMapper
                );

        Patient patient = new Patient();

        PatientResponse response =
                new PatientResponse(
                        100L,
                        1L,
                        "Marty McFly",
                        "12345678901",
                        LocalDate.of(1985, 7, 3),
                        "11999999999",
                        PaymentType.PRIVATE,
                        null,
                        null
                );

        when(patientService.findById(patientId))
                .thenReturn(Optional.of(patient));

        when(patientMapper.toResponse(patient))
                .thenReturn(response);

        // Act
        ResponseEntity<PatientResponse> result =
                patientController.findById(patientId);

        // Assert
        assertEquals(200, result.getStatusCode().value());
        assertSame(response, result.getBody());

        verify(patientService).findById(patientId);
        verify(patientMapper).toResponse(patient);
    }

    @Test
    void shouldReturnNotFoundWhenPatientDoesNotExist() {

        // Arrange
        Long patientId = 999L;

        PatientService patientService =
                mock(PatientService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientController patientController =
                new PatientController(
                        patientService,
                        patientMapper
                );

        when(patientService.findById(patientId))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<PatientResponse> result =
                patientController.findById(patientId);

        // Assert
        assertEquals(404, result.getStatusCode().value());
        assertNull(result.getBody());

        verify(patientService).findById(patientId);
        verifyNoInteractions(patientMapper);
    }

    @Test
    void shouldUpdatePatient() {

        // Arrange
        Long patientId = 100L;

        PatientUpdateRequest request =
                new PatientUpdateRequest(
                        "98765432100",
                        LocalDate.of(1985, 7, 3),
                        "11988887777",
                        PaymentType.PRIVATE,
                        null
                );

        Patient patient = new Patient();
        patient.setId(patientId);

        PatientResponse response = mock(PatientResponse.class);

        PatientService patientService = mock(PatientService.class);

        PatientMapper patientMapper = mock(PatientMapper.class);

        PatientController patientController =
                new PatientController(patientService, patientMapper);

        when(patientService.update(patientId, request))
                .thenReturn(patient);

        when(patientMapper.toResponse(patient))
                .thenReturn(response);

        // Act
        ResponseEntity<PatientResponse> result =
                patientController.update(patientId, request);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertSame(response, result.getBody());

        verify(patientService).update(patientId, request);
        verify(patientMapper).toResponse(patient);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingMissingPatient() {

        // Arrange
        Long patientId = 999L;

        PatientService patientService =
                mock(PatientService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientController patientController =
                new PatientController(
                        patientService,
                        patientMapper
                );

        PatientUpdateRequest request =
                mock(PatientUpdateRequest.class);

        when(patientService.update(patientId, request))
                .thenThrow(new PatientNotFoundException());

        // Act & Assert
        assertThrows(
                PatientNotFoundException.class,
                () -> patientController.update(patientId, request)
        );

        verify(patientService).update(patientId, request);
        verifyNoInteractions(patientMapper);
    }

    @Test
    void shouldDeletePatient() {

        // Arrange
        Long patientId = 100L;

        PatientService patientService =
                mock(PatientService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientController patientController =
                new PatientController(
                        patientService,
                        patientMapper
                );

        // Act
        ResponseEntity<Void> result =
                patientController.delete(patientId);

        // Assert
        assertEquals(
                HttpStatus.NO_CONTENT,
                result.getStatusCode()
        );

        assertNull(result.getBody());

        verify(patientService).delete(patientId);
        verifyNoInteractions(patientMapper);
    }

    @Test
    void shouldThrowExceptionWhenDeletingMissingPatient() {

        // Arrange
        Long patientId = 999L;

        PatientService patientService =
                mock(PatientService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientController patientController =
                new PatientController(
                        patientService,
                        patientMapper
                );

        doThrow(new PatientNotFoundException())
                .when(patientService)
                .delete(patientId);

        // Act & Assert
        assertThrows(
                PatientNotFoundException.class,
                () -> patientController.delete(patientId)
        );

        verify(patientService).delete(patientId);
        verifyNoInteractions(patientMapper);
    }

    @Test
    void shouldReturnBadRequestWhenCreatingPatientWithBlankCpf()
            throws Exception {

        // Arrange
        PatientService patientService =
                mock(PatientService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientController patientController =
                new PatientController(
                        patientService,
                        patientMapper
                );

        MockMvc mockMvc =
                MockMvcBuilders
                        .standaloneSetup(patientController)
                        .setControllerAdvice(new GlobalExceptionHandler())
                        .build();

        // Arrange
        String requestBody = """
                {
                    "userId": 1,
                    "cpf": "",
                    "birthDate": "1990-05-15",
                    "phone": "11999999999",
                    "paymentType": "PRIVATE",
                    "healthPlanId": null
                }
                """;

        // Act & Assert
        mockMvc.perform(
                        post("/api/patients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.errors.cpf")
                                .value("must not be blank")
                );

        verifyNoInteractions(patientService);
    }

    @Test
    void shouldReturnBadRequestWithMultipleValidationErrors()
            throws Exception {

        // Arrange
        PatientService patientService =
                mock(PatientService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientController patientController =
                new PatientController(
                        patientService,
                        patientMapper
                );

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(patientController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        String requestBody = """
                {
                    "userId": 1,
                    "cpf": "",
                    "birthDate": "1990-05-15",
                    "phone": "",
                    "paymentType": "PRIVATE",
                    "healthPlanId": null
                }
                """;

        // Act & Assert
        mockMvc.perform(
                        post("/api/patients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.errors.cpf")
                                .value("must not be blank")
                )
                .andExpect(
                        jsonPath("$.errors.phone")
                                .value("must not be blank")
                );

        verifyNoInteractions(patientService);
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingPatientWithBlankCpf()
            throws Exception {

        // Arrange
        PatientService patientService = mock(PatientService.class);
        PatientMapper patientMapper = mock(PatientMapper.class);

        PatientController patientController =
                new PatientController(patientService, patientMapper);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(patientController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        String requestBody = """
                {
                    "cpf": "",
                    "birthDate": "1990-05-15",
                    "phone": "11999999999",
                    "paymentType": "PRIVATE",
                    "healthPlanId": null
                }
                """;

        // Act & Assert
        mockMvc.perform(
                        put("/api/patients/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.errors.cpf")
                                .value("must not be blank")
                );

        verifyNoInteractions(patientService);
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingPatientWithFutureBirthDate()
            throws Exception {

        // Arrange
        PatientService patientService = mock(PatientService.class);
        PatientMapper patientMapper = mock(PatientMapper.class);

        PatientController patientController =
                new PatientController(patientService, patientMapper);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(patientController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        String requestBody = """
            {
                "cpf": "12345678901",
                "birthDate": "2099-05-15",
                "phone": "11999999999",
                "paymentType": "PRIVATE",
                "healthPlanId": null
            }
            """;

        // Act & Assert
        mockMvc.perform(
                        put("/api/patients/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.errors.birthDate")
                                .value("must be a past date")
                );

        verifyNoInteractions(patientService);
    }
}
