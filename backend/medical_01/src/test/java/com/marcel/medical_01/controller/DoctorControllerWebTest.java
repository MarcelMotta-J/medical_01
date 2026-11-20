package com.marcel.medical_01.controller;

import com.marcel.medical_01.dto.doctor.DoctorUpdateRequest;
import org.junit.jupiter.api.Test;

import com.marcel.medical_01.service.DoctorService;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.marcel.medical_01.model.Doctor;
import com.marcel.medical_01.model.User;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.marcel.medical_01.exception.GlobalExceptionHandler;

import com.marcel.medical_01.exception.DoctorNotFoundException;

import static org.mockito.Mockito.doThrow;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class DoctorControllerWebTest {

    @Test
    void shouldUpdateDoctorAndReturnOk() throws Exception {

        DoctorService doctorService = mock(DoctorService.class);

        User user = new User();
        user.setId(10L);
        user.setName("Dr. Emmett Brown");

        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setUser(user);
        doctor.setCrm("987654");
        doctor.setCrmUf("RJ");

        when(doctorService.update(
                eq(1L),
                any(DoctorUpdateRequest.class)
        )).thenReturn(doctor);

        DoctorController doctorController = new DoctorController(doctorService);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(doctorController)
                .build();

        mockMvc.perform(
                        put("/api/doctors/{id}", 1L)
                                .contentType("application/json")
                                .content("""
                                        {
                                            "crm": "987654",
                                            "crmUf": "RJ"
                                        }
                                        """)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(10))
                .andExpect(jsonPath("$.name").value("Dr. Emmett Brown"))
                .andExpect(jsonPath("$.crm").value("987654"))
                .andExpect(jsonPath("$.crmUf").value("RJ"))
                .andExpect(jsonPath("$.active").value(true))
                .andDo(result -> {

                    System.out.println("[DoctorControllerWebTest] HTTP status: "
                            + result.getResponse().getStatus());

                    System.out.println("[DoctorControllerWebTest] Response: "
                            + result.getResponse().getContentAsString());

                });
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingDoctorWithInvalidData()
            throws Exception {

        DoctorService doctorService = mock(DoctorService.class);

        DoctorController doctorController = new DoctorController(doctorService);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(doctorController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        mockMvc.perform(
                        put("/api/doctors/{id}", 1L)
                                .contentType("application/json")
                                .content("""
                                        {
                                            "crm": "",
                                            "crmUf": "rj"
                                        }
                                        """)
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.crm").value("must not be blank"))
                .andExpect(jsonPath("$.errors.crmUf").value("must match \"[A-Z]{2}\""))
                .andDo(result -> {

                    System.out.println("[DoctorControllerWebTest] Invalid data - HTTP status: "
                            + result.getResponse().getStatus());

                    System.out.println("[DoctorControllerWebTest] Validation errors: "
                            + result.getResponse().getContentAsString());

                });

        verifyNoInteractions(doctorService);
    }

    @Test
    void shouldDeactivateDoctorAndReturnNoContent() throws Exception {

        DoctorService doctorService = mock(DoctorService.class);

        DoctorController doctorController =
                new DoctorController(doctorService);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(doctorController)
                .build();

        mockMvc.perform(
                        patch("/api/doctors/{id}/deactivate", 1L)
                )
                .andExpect(status().isNoContent());

        verify(doctorService).deactivate(1L);
    }

    @Test
    void shouldReturnNotFoundWhenDeactivatingNonExistingDoctor()
            throws Exception {

        Long doctorId = 999L;

        DoctorService doctorService = mock(DoctorService.class);

        DoctorController doctorController =
                new DoctorController(doctorService);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(doctorController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        doThrow(new DoctorNotFoundException())
                .when(doctorService)
                .deactivate(doctorId);

        mockMvc.perform(
                        patch("/api/doctors/{id}/deactivate", doctorId)
                )
                .andExpect(status().isNotFound());

        verify(doctorService).deactivate(doctorId);
    }

    @Test
    void shouldReturnInactiveDoctorInJson() throws Exception {

        Long doctorId = 1L;

        DoctorService doctorService = mock(DoctorService.class);

        User user = new User();
        user.setId(10L);
        user.setName("Dr. Emmett Brown");

        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        doctor.setUser(user);
        doctor.setCrm("987654");
        doctor.setCrmUf("RJ");

        doctor.setActive(false);

        when(doctorService.findById(doctorId))
                .thenReturn(Optional.of(doctor));

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(new DoctorController(doctorService))
                .build();

        mockMvc.perform(
                        get("/api/doctors/{id}", doctorId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));

        verify(doctorService).findById(doctorId);
    }

    @Test
    void shouldActivateDoctorAndReturnNoContent() throws Exception {

        Long doctorId = 1L;

        DoctorService doctorService =
                mock(DoctorService.class);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(new DoctorController(doctorService))
                .build();

        mockMvc.perform(
                        patch("/api/doctors/{id}/activate", doctorId)
                )
                .andExpect(status().isNoContent());

        verify(doctorService).activate(doctorId);
    }

    @Test
    void shouldReturnNotFoundWhenActivatingNonExistingDoctor() throws Exception {

        Long doctorId = 999L;

        DoctorService doctorService =
                mock(DoctorService.class);

        doThrow(new DoctorNotFoundException())
                .when(doctorService)
                .activate(doctorId);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(new DoctorController(doctorService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        mockMvc.perform(
                        patch("/api/doctors/{id}/activate", doctorId)
                )
                .andExpect(status().isNotFound());

        verify(doctorService).activate(doctorId);
    }
}
