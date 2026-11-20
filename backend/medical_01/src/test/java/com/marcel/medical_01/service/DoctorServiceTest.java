package com.marcel.medical_01.service;

import com.marcel.medical_01.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.marcel.medical_01.model.Doctor;

import java.util.Optional;

import java.util.List;

import com.marcel.medical_01.dto.doctor.DoctorRequest;
import com.marcel.medical_01.model.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.marcel.medical_01.model.Role;

import com.marcel.medical_01.dto.doctor.DoctorUpdateRequest;

import com.marcel.medical_01.exception.DoctorNotFoundException;

import com.marcel.medical_01.service.UserService;

import static org.mockito.Mockito.verify;

import static org.mockito.Mockito.never;


class DoctorServiceTest {

    @Test
    void shouldFindDoctorById() {

        DoctorRepository doctorRepository =
                mock(DoctorRepository.class);

        UserService userService =
                mock(UserService.class);

        DoctorService doctorService =
                new DoctorService(doctorRepository, userService);

        Doctor doctor = new Doctor();
        doctor.setId(1L);

        when(doctorRepository.findById(1L))
                .thenReturn(Optional.of(doctor));

        Optional<Doctor> result = doctorService.findById(1L);

        assertEquals(doctor, result.orElseThrow());

        System.out.println("[DoctorServiceTest] Doctor found (mock) => ID: "
                + result.orElseThrow().getId());
    }

    @Test
    void shouldReturnEmptyWhenDoctorNotFound() {

        DoctorRepository doctorRepository =
                mock(DoctorRepository.class);

        UserService userService =
                mock(UserService.class);

        DoctorService doctorService =
                new DoctorService(doctorRepository, userService);

        when(doctorRepository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<Doctor> result = doctorService.findById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindAllDoctors() {

        // 1. Create the mocked repository
        DoctorRepository doctorRepository =
                mock(DoctorRepository.class);

        UserService userService =
                mock(UserService.class);

        DoctorService doctorService =
                new DoctorService(doctorRepository, userService);

        // 2. Create two doctors in memory
        Doctor doctor1 = new Doctor();
        doctor1.setId(1L);
        doctor1.setCrm("123456");
        doctor1.setCrmUf("SP");

        Doctor doctor2 = new Doctor();
        doctor2.setId(2L);
        doctor2.setCrm("654321");
        doctor2.setCrmUf("RJ");

        // 3. Configure the mock
        when(doctorRepository.findAll())
                .thenReturn(List.of(doctor1, doctor2));

        // 4. Call the service
        List<Doctor> result = doctorService.findAll();

        // 5. Verify the result
        assertEquals(2, result.size());

        // 6. Print the mocked doctors
        System.out.println("\n[DoctorServiceTest] Mocked doctors:");

        for (Doctor doctor : result) {
            System.out.println(
                    "Doctor ID: " + doctor.getId()
                            + " | CRM: " + doctor.getCrm()
                            + " | UF: " + doctor.getCrmUf()
            );
        }
    }

    @Test
    void shouldFindUserFromDoctorRequest() {

        // 1. Create mocks
        DoctorRepository doctorRepository =
                mock(DoctorRepository.class);

        UserService userService =
                mock(UserService.class);

        DoctorService doctorService =
                new DoctorService(doctorRepository, userService);

        // 2. Create a user in memory
        User user = new User();
        user.setId(10L);
        user.setName("Dr. Brown");
        user.setRole(Role.DOCTOR);

        // 3. Create the request
        DoctorRequest request =
                new DoctorRequest(10L, "123456", "SP");

        // 4. Configure the mock
        when(userService.findById(10L))
                .thenReturn(Optional.of(user));

        // 5. Call the service
        User result = doctorService.findUserFromRequest(request);

        // 6. Verify the result
        assertEquals(user, result);

        // 7. Print the result
        System.out.println(
                "[DoctorServiceTest] User found => ID: "
                        + result.getId()
                        + " | Name: " + result.getName()
        );
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        // 1. Create mocks
        DoctorRepository doctorRepository =
                mock(DoctorRepository.class);

        UserService userService =
                mock(UserService.class);

        DoctorService doctorService =
                new DoctorService(doctorRepository, userService);

        // 2. Create a request with a nonexistent user
        DoctorRequest request =
                new DoctorRequest(99L, "123456", "SP");

        // 3. Configure the mock
        when(userService.findById(99L))
                .thenReturn(Optional.empty());

        // 4. Verify that the exception is thrown
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> doctorService.findUserFromRequest(request)
                );

        // 5. Verify the error message
        assertEquals("User not found.", exception.getMessage());

        // 6. Print the result
        System.out.println(
                "[DoctorServiceTest] Expected exception => "
                        + exception.getMessage()
        );
    }

    @Test
    void shouldRejectPatientUserAsDoctor() {

        // 1. Create mocks
        DoctorRepository doctorRepository =
                mock(DoctorRepository.class);

        UserService userService =
                mock(UserService.class);

        DoctorService doctorService =
                new DoctorService(doctorRepository, userService);

        // 2. Create a user with PATIENT role
        User user = new User();
        user.setId(20L);
        user.setName("Marty McFly");
        user.setRole(Role.PATIENT);

        // 3. Create a doctor registration request
        DoctorRequest request =
                new DoctorRequest(20L, "123456", "SP");

        // 4. Configure the mock
        when(userService.findById(20L))
                .thenReturn(Optional.of(user));

        // 5. Verify that the service rejects the patient
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> doctorService.findUserFromRequest(request)
                );

        // 6. Verify the error message
        assertEquals(
                "User must have DOCTOR role.",
                exception.getMessage()
        );

        // 7. Print the expected result
        System.out.println(
                "[DoctorServiceTest] PATIENT rejected => "
                        + exception.getMessage()
        );
    }

    @Test
    void shouldCreateDoctor() {

        // 1. Create mocks
        DoctorRepository doctorRepository =
                mock(DoctorRepository.class);

        UserService userService =
                mock(UserService.class);

        DoctorService doctorService =
                new DoctorService(doctorRepository, userService);

        // 2. Create a user with DOCTOR role
        User user = new User();
        user.setId(10L);
        user.setName("Dr. Brown");
        user.setRole(Role.DOCTOR);

        // 3. Create the request
        DoctorRequest request =
                new DoctorRequest(10L, "123456", "SP");

        // 4. Configure the UserService mock
        when(userService.findById(10L))
                .thenReturn(Optional.of(user));

        // 5. Configure the repository mock
        when(doctorRepository.save(any(Doctor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // 6. Call the service
        Doctor result = doctorService.create(request);

        // 7. Verify the saved doctor information
        assertEquals(user, result.getUser());
        assertEquals("123456", result.getCrm());
        assertEquals("SP", result.getCrmUf());

        // 8. Verify that save() was called exactly once
        Mockito.verify(doctorRepository, Mockito.times(1))
                .save(any(Doctor.class));

        // 9. Print the result
        System.out.println(
                "[DoctorServiceTest] Doctor created => "
                        + result.getUser().getName()
                        + " | CRM: " + result.getCrm()
                        + " | UF: " + result.getCrmUf()
        );
    }

    @Test
    void shouldNotSaveDoctorWhenUserIsPatient() {

        // 1. Create mocks
        DoctorRepository doctorRepository =
                mock(DoctorRepository.class);

        UserService userService =
                mock(UserService.class);

        DoctorService doctorService =
                new DoctorService(doctorRepository, userService);

        // 2. Create a patient user
        User user = new User();
        user.setId(20L);
        user.setName("Marty McFly");
        user.setRole(Role.PATIENT);

        // 3. Create a doctor registration request
        DoctorRequest request =
                new DoctorRequest(20L, "123456", "SP");

        // 4. Configure the user lookup
        when(userService.findById(20L))
                .thenReturn(Optional.of(user));

        // 5. Verify that create() rejects the patient
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> doctorService.create(request)
                );

        assertEquals(
                "User must have DOCTOR role.",
                exception.getMessage()
        );

        // 6. Verify that nothing was saved
        Mockito.verify(doctorRepository, Mockito.never())
                .save(any(Doctor.class));

        System.out.println(
                "[DoctorServiceTest] Doctor not saved => "
                        + exception.getMessage()
        );
    }

    @Test
    void shouldNotSaveDoctorWhenUserNotFound() {

        // 1. Create mocks
        DoctorRepository doctorRepository =
                mock(DoctorRepository.class);

        UserService userService =
                mock(UserService.class);

        DoctorService doctorService =
                new DoctorService(doctorRepository, userService);

        // 2. Create a request with a nonexistent user
        DoctorRequest request =
                new DoctorRequest(99L, "123456", "SP");

        // 3. Configure the mock
        when(userService.findById(99L))
                .thenReturn(Optional.empty());

        // 4. Verify the expected exception
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> doctorService.create(request)
                );

        assertEquals("User not found.", exception.getMessage());

        // 5. Verify that no doctor was saved
        Mockito.verify(doctorRepository, Mockito.never())
                .save(any(Doctor.class));

        // 6. Print the result
        System.out.println(
                "[DoctorServiceTest] Doctor not saved => "
                        + exception.getMessage()
        );
    }

    @Test
    void shouldUpdateExistingDoctor() {

        // Arrange - Create mocks
        DoctorRepository doctorRepository =
                mock(DoctorRepository.class);

        UserService userService =
                mock(UserService.class);

        DoctorService doctorService =
                new DoctorService(doctorRepository, userService);

        // Existing doctor
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setCrm("123456");
        doctor.setCrmUf("SP");

        // New information
        DoctorUpdateRequest request =
                new DoctorUpdateRequest("987654", "RJ");

        // Mock - Find the existing doctor
        when(doctorRepository.findById(1L))
                .thenReturn(Optional.of(doctor));

        // Mock - Save the updated doctor
        when(doctorRepository.save(any(Doctor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        System.out.println(
                "[DoctorServiceTest] Before update => CRM: "
                        + doctor.getCrm()
                        + " | UF: " + doctor.getCrmUf()
        );

        // Act - Update the doctor
        Doctor updatedDoctor = doctorService.update(1L, request);

        // Assert - Verify updated information
        assertEquals("987654", updatedDoctor.getCrm());
        assertEquals("RJ", updatedDoctor.getCrmUf());

        // Verify - Repository save must be called once
        Mockito.verify(doctorRepository, Mockito.times(1))
                .save(doctor);

        System.out.println(
                "[DoctorServiceTest] After update => CRM: "
                        + doctor.getCrm()
                        + " | UF: " + doctor.getCrmUf()
        );
    }

    @Test
    void shouldThrowExceptionWhenUpdatingDoctorNotFound() {

        // Arrange - Create mocks
        DoctorRepository doctorRepository =
                mock(DoctorRepository.class);

        UserService userService =
                mock(UserService.class);

        DoctorService doctorService =
                new DoctorService(doctorRepository, userService);

        // Doctor does not exist
        when(doctorRepository.findById(99L))
                .thenReturn(Optional.empty());

        DoctorUpdateRequest request =
                new DoctorUpdateRequest("987654", "RJ");

        // Act and Assert
        DoctorNotFoundException exception = assertThrows(
                DoctorNotFoundException.class,
                () -> doctorService.update(99L, request)
        );

        assertEquals("Doctor not found.", exception.getMessage());

        System.out.println(
                "[DoctorServiceTest] Update rejected => "
                        + exception.getMessage()
        );

        // Verify - A nonexistent doctor must not be saved
        Mockito.verify(doctorRepository, Mockito.never())
                .save(any(Doctor.class));
    }

    @Test
    void shouldDeactivateExistingDoctor() {

        Long doctorId = 1L;

        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        doctor.setActive(true);

        DoctorRepository doctorRepository = mock(DoctorRepository.class);
        UserService userService = mock(UserService.class);

        DoctorService doctorService = new DoctorService(
                doctorRepository,
                userService
        );

        when(doctorRepository.findById(doctorId))
                .thenReturn(Optional.of(doctor));

        System.out.println(
                "[DoctorServiceTest] Before deactivation => Active: "
                        + doctor.isActive()
        );

        doctorService.deactivate(doctorId);

        assertFalse(doctor.isActive());

        verify(doctorRepository).save(doctor);
    }

    @Test
    void shouldThrowExceptionWhenDeactivatingNonExistingDoctor() {

        Long doctorId = 999L;

        DoctorRepository doctorRepository = mock(DoctorRepository.class);
        UserService userService = mock(UserService.class);

        DoctorService doctorService = new DoctorService(
                doctorRepository,
                userService
        );

        when(doctorRepository.findById(doctorId))
                .thenReturn(Optional.empty());

        assertThrows(
                DoctorNotFoundException.class,
                () -> doctorService.deactivate(doctorId)
        );

        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    void shouldActivateExistingDoctor() {

        Long doctorId = 1L;

        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        doctor.setActive(false);

        DoctorRepository doctorRepository =
                mock(DoctorRepository.class);

        UserService userService =
                mock(UserService.class);

        DoctorService doctorService =
                new DoctorService(doctorRepository, userService);

        when(doctorRepository.findById(doctorId))
                .thenReturn(Optional.of(doctor));

        doctorService.activate(doctorId);

        assertTrue(doctor.isActive());

        verify(doctorRepository).save(doctor);
    }

    @Test
    void shouldThrowExceptionWhenActivatingNonExistingDoctor() {

        Long doctorId = 999L;

        DoctorRepository doctorRepository =
                mock(DoctorRepository.class);

        UserService userService =
                mock(UserService.class);

        DoctorService doctorService =
                new DoctorService(doctorRepository, userService);

        when(doctorRepository.findById(doctorId))
                .thenReturn(Optional.empty());

        assertThrows(
                DoctorNotFoundException.class,
                () -> doctorService.activate(doctorId)
        );

        verify(doctorRepository, never())
                .save(any(Doctor.class));
    }
}
