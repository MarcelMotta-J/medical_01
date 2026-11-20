package com.marcel.medical_01.controller;

import com.marcel.medical_01.dto.doctor.DoctorRequest;
import com.marcel.medical_01.dto.doctor.DoctorResponse;
import com.marcel.medical_01.model.Doctor;
import com.marcel.medical_01.model.User;
import com.marcel.medical_01.service.DoctorService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;

import com.marcel.medical_01.dto.doctor.DoctorUpdateRequest;

class DoctorControllerTest {

    @Test
    void shouldCreateDoctorAndReturnCreated() {

        // 1. Mock the service
        DoctorService doctorService =
                Mockito.mock(DoctorService.class);

        DoctorController controller =
                new DoctorController(doctorService);

        // 2. Prepare test data
        DoctorRequest request =
                new DoctorRequest(10L, "123456", "SP");

        User user = new User();
        user.setId(10L);
        user.setName("Dr. Emmett Brown");

        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setUser(user);
        doctor.setCrm("123456");
        doctor.setCrmUf("SP");

        // 3. Configure the mock
        Mockito.when(doctorService.create(request))
                .thenReturn(doctor);

        // 4. Call the controller
        ResponseEntity<DoctorResponse> result =
                controller.create(request);

        // 5. Verify HTTP status and response
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(1L, result.getBody().id());
        assertEquals(10L, result.getBody().userId());
        assertEquals("Dr. Emmett Brown", result.getBody().name());
        assertEquals("123456", result.getBody().crm());
        assertEquals("SP", result.getBody().crmUf());

        Mockito.verify(doctorService).create(request);

        System.out.println(
                "[DoctorControllerTest] HTTP 201 => "
                        + result.getBody().name()
        );
    }

    @Test
    void shouldFindAllDoctors() {

        // 1. Mock the service
        DoctorService doctorService =
                Mockito.mock(DoctorService.class);

        DoctorController controller =
                new DoctorController(doctorService);

        // 2. Create users
        User user1 = new User();
        user1.setId(10L);
        user1.setName("Dr. Emmett Brown");

        User user2 = new User();
        user2.setId(20L);
        user2.setName("Dr. Stephen Strange");

        // 3. Create doctors
        Doctor doctor1 = new Doctor();
        doctor1.setId(1L);
        doctor1.setUser(user1);
        doctor1.setCrm("123456");
        doctor1.setCrmUf("SP");

        Doctor doctor2 = new Doctor();
        doctor2.setId(2L);
        doctor2.setUser(user2);
        doctor2.setCrm("654321");
        doctor2.setCrmUf("RJ");

        // 4. Configure the mock
        Mockito.when(doctorService.findAll())
                .thenReturn(List.of(doctor1, doctor2));

        // 5. Call the controller
        ResponseEntity<List<DoctorResponse>> result =
                controller.findAll();

        // 6. Verify HTTP status and response
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().size());

        assertEquals("Dr. Emmett Brown",
                result.getBody().get(0).name());

        assertEquals("Dr. Stephen Strange",
                result.getBody().get(1).name());

        Mockito.verify(doctorService).findAll();

        // 7. Print results
        result.getBody().forEach(doctor ->
                System.out.println(
                        "[DoctorControllerTest] Doctor => "
                                + doctor.name()
                                + " | CRM: "
                                + doctor.crm()
                                + "/" + doctor.crmUf()
                )
        );
    }

    @Test
    void shouldReturnEmptyListWhenNoDoctorsExist() {

        // 1. Mock the service
        DoctorService doctorService =
                Mockito.mock(DoctorService.class);

        DoctorController controller =
                new DoctorController(doctorService);

        // 2. Configure an empty list
        Mockito.when(doctorService.findAll())
                .thenReturn(List.of());

        // 3. Call the controller
        ResponseEntity<List<DoctorResponse>> result =
                controller.findAll();

        // 4. Verify HTTP status and empty list
        assertEquals(HttpStatus.OK, result.getStatusCode());

        assertNotNull(result.getBody());

        assertTrue(result.getBody().isEmpty());

        // 5. Verify service interaction
        Mockito.verify(doctorService).findAll();

        System.out.println(
                "[DoctorControllerTest] HTTP 200 => Empty doctor list"
        );
    }

    @Test
    void shouldFindDoctorById() {

        // 1. Mock the service
        DoctorService doctorService =
                Mockito.mock(DoctorService.class);

        DoctorController controller =
                new DoctorController(doctorService);

        // 2. Create a user
        User user = new User();
        user.setId(10L);
        user.setName("Dr. Emmett Brown");

        // 3. Create a doctor
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setUser(user);
        doctor.setCrm("123456");
        doctor.setCrmUf("SP");

        // 4. Configure the mock
        Mockito.when(doctorService.findById(1L))
                .thenReturn(Optional.of(doctor));

        // 5. Call the controller
        ResponseEntity<DoctorResponse> result =
                controller.findById(1L);

        // 6. Verify HTTP status and response
        assertEquals(HttpStatus.OK, result.getStatusCode());

        assertNotNull(result.getBody());

        assertEquals(1L, result.getBody().id());
        assertEquals(10L, result.getBody().userId());
        assertEquals("Dr. Emmett Brown", result.getBody().name());
        assertEquals("123456", result.getBody().crm());
        assertEquals("SP", result.getBody().crmUf());

        // 7. Verify service interaction
        Mockito.verify(doctorService).findById(1L);

        System.out.println(
                "[DoctorControllerTest] HTTP 200 => "
                        + result.getBody().name()
        );
    }

    @Test
    void shouldReturnNotFoundWhenDoctorDoesNotExist() {

        // 1. Mock the service
        DoctorService doctorService =
                Mockito.mock(DoctorService.class);

        DoctorController controller =
                new DoctorController(doctorService);

        // 2. Configure doctor not found
        Mockito.when(doctorService.findById(99L))
                .thenReturn(Optional.empty());

        // 3. Call the controller
        ResponseEntity<DoctorResponse> result =
                controller.findById(99L);

        // 4. Verify HTTP 404
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        // 5. Verify empty response body
        assertNull(result.getBody());

        // 6. Verify service interaction
        Mockito.verify(doctorService).findById(99L);

        System.out.println(
                "[DoctorControllerTest] HTTP 404 => Doctor not found"
        );
    }

    @Test
    void shouldUpdateExistingDoctor() {

        // 1. Mock the service
        DoctorService doctorService =
                Mockito.mock(DoctorService.class);

        DoctorController controller =
                new DoctorController(doctorService);

        // 2. Create a user
        User user = new User();
        user.setId(10L);
        user.setName("Dr. Emmett Brown");

        // 3. Prepare updated doctor
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setUser(user);
        doctor.setCrm("987654");
        doctor.setCrmUf("RJ");

        // 4. Prepare update request
        DoctorUpdateRequest request =
                new DoctorUpdateRequest("987654", "RJ");

        // 5. Configure the mock
        Mockito.when(doctorService.update(1L, request))
                .thenReturn(doctor);

        // 6. Call the controller
        ResponseEntity<DoctorResponse> result =
                controller.update(1L, request);

        // 7. Verify HTTP status and response
        assertEquals(HttpStatus.OK, result.getStatusCode());

        assertNotNull(result.getBody());

        assertEquals(1L, result.getBody().id());
        assertEquals(10L, result.getBody().userId());
        assertEquals("Dr. Emmett Brown", result.getBody().name());
        assertEquals("987654", result.getBody().crm());
        assertEquals("RJ", result.getBody().crmUf());

        // 8. Verify service interaction
        Mockito.verify(doctorService, Mockito.times(1))
                .update(1L, request);

        System.out.println(
                "[DoctorControllerTest] HTTP 200 => "
                        + result.getBody().name()
                        + " | CRM: " + result.getBody().crm()
                        + " | UF: " + result.getBody().crmUf()
        );
    }
}
