package com.marcel.medical_01.service;

import com.marcel.medical_01.model.Patient;
import com.marcel.medical_01.model.PaymentType;
import com.marcel.medical_01.repository.PatientRepository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.marcel.medical_01.model.HealthPlan;

import com.marcel.medical_01.mapper.PatientMapper;

import com.marcel.medical_01.dto.patient.PatientRequest;
import com.marcel.medical_01.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

import java.util.List;

import com.marcel.medical_01.dto.patient.PatientUpdateRequest;

import com.marcel.medical_01.exception.PatientNotFoundException;

class PatientServiceTest {

    // PRIVATE + HealthPlan == null → ok ✅
    // PRIVATE sem plano e um cenario valido.
    @Test
    void shouldSavePrivatePatientWithoutHealthPlan() {

        PatientRepository patientRepository =
                mock(PatientRepository.class);

        UserService userService =
                mock(UserService.class);

        HealthPlanService healthPlanService =
                mock(HealthPlanService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientService patientService =
                new PatientService(
                        patientRepository,
                        userService,
                        healthPlanService,
                        patientMapper
                );

        Patient patient = new Patient();
        patient.setPaymentType(PaymentType.PRIVATE);
        patient.setHealthPlan(null);

        when(patientRepository.save(patient))
                .thenReturn(patient);

        Patient savedPatient = patientService.save(patient);

        assertSame(patient, savedPatient);
    }

    // PRIVATE + HealthPlan → exception ❌
    // PRIVATE nao pode carregar HealthPlan.
    @Test
    void shouldRejectPrivatePatientWithHealthPlan() {

        PatientRepository patientRepository =
                mock(PatientRepository.class);

        UserService userService =
                mock(UserService.class);

        HealthPlanService healthPlanService =
                mock(HealthPlanService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientService patientService =
                new PatientService(
                        patientRepository,
                        userService,
                        healthPlanService,
                        patientMapper
                );

        Patient patient = new Patient();
        patient.setPaymentType(PaymentType.PRIVATE);
        patient.setHealthPlan(new HealthPlan());

        assertThrows(
                IllegalArgumentException.class,
                () -> patientService.save(patient)
        );
    }

    // HEALTH_INSURANCE + HealthPlan ✅
    // HEALTH_INSURANCE com plano ativo e valido.
    @Test
    void shouldSaveHealthInsurancePatientWithHealthPlan() {

        PatientRepository patientRepository =
                mock(PatientRepository.class);

        UserService userService =
                mock(UserService.class);

        HealthPlanService healthPlanService =
                mock(HealthPlanService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientService patientService =
                new PatientService(
                        patientRepository,
                        userService,
                        healthPlanService,
                        patientMapper
                );

        HealthPlan healthPlan = new HealthPlan();
        healthPlan.setActive(true);

        Patient patient = new Patient();
        patient.setPaymentType(PaymentType.HEALTH_INSURANCE);
        patient.setHealthPlan(healthPlan);

        when(patientRepository.save(patient))
                .thenReturn(patient);

        Patient savedPatient = patientService.save(patient);

        assertSame(patient, savedPatient);
    }

    //HEALTH_INSURANCE + null ❌
    // HEALTH_INSURANCE exige HealthPlan.
    @Test
    void shouldRejectHealthInsurancePatientWithoutHealthPlan() {

        PatientRepository patientRepository =
                mock(PatientRepository.class);

        UserService userService =
                mock(UserService.class);

        HealthPlanService healthPlanService =
                mock(HealthPlanService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientService patientService =
                new PatientService(
                        patientRepository,
                        userService,
                        healthPlanService,
                        patientMapper
                );

        Patient patient = new Patient();
        patient.setPaymentType(PaymentType.HEALTH_INSURANCE);
        patient.setHealthPlan(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> patientService.save(patient)
        );
    }

    // HEALTH_INSURANCE + inactive HealthPlan → exception ❌
    // HEALTH_INSURANCE requires an active HealthPlan.
    // Test-Driven Development (TDD): test written before the business rule.
    @Test
    void shouldRejectHealthInsurancePatientWithInactiveHealthPlan() {

        PatientRepository patientRepository =
                mock(PatientRepository.class);

        UserService userService =
                mock(UserService.class);

        HealthPlanService healthPlanService =
                mock(HealthPlanService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientService patientService =
                new PatientService(
                        patientRepository,
                        userService,
                        healthPlanService,
                        patientMapper
                );
        HealthPlan healthPlan = new HealthPlan();
        healthPlan.setActive(false);

        Patient patient = new Patient();
        patient.setPaymentType(PaymentType.HEALTH_INSURANCE);
        patient.setHealthPlan(healthPlan);

        assertThrows(
                IllegalArgumentException.class,
                () -> patientService.save(patient)
        );
    }

    // PRIVATE → healthPlanId null
    @Test
    void shouldCreatePrivatePatientFromRequest() {

        PatientRepository patientRepository =
                mock(PatientRepository.class);

        UserService userService =
                mock(UserService.class);

        HealthPlanService healthPlanService =
                mock(HealthPlanService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientService patientService =
                new PatientService(
                        patientRepository,
                        userService,
                        healthPlanService,
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

        User user = new User();
        user.setId(1L);

        Patient patient = new Patient();
        patient.setPaymentType(PaymentType.PRIVATE);

        when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        when(patientMapper.toEntity(
                request,
                user,
                null
        )).thenReturn(patient);

        when(patientRepository.save(patient))
                .thenReturn(patient);

        Patient createdPatient =
                patientService.create(request);

        assertSame(patient, createdPatient);
    }

    // HEALTH_INSURANCE → busca HealthPlan → plano ativo → save
    @Test
    void shouldCreateHealthInsurancePatientFromRequest() {

        PatientRepository patientRepository =
                mock(PatientRepository.class);

        UserService userService =
                mock(UserService.class);

        HealthPlanService healthPlanService =
                mock(HealthPlanService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientService patientService =
                new PatientService(
                        patientRepository,
                        userService,
                        healthPlanService,
                        patientMapper
                );

        PatientRequest request =
                new PatientRequest(
                        1L,
                        "12345678901",
                        LocalDate.of(1985, 7, 3),
                        "11999999999",
                        PaymentType.HEALTH_INSURANCE,
                        10L
                );

        User user = new User();
        user.setId(1L);

        HealthPlan healthPlan = new HealthPlan();
        healthPlan.setId(10L);
        healthPlan.setName("Unimed");
        healthPlan.setActive(true);

        Patient patient = new Patient();
        patient.setPaymentType(PaymentType.HEALTH_INSURANCE);
        patient.setHealthPlan(healthPlan);

        when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        when(healthPlanService.findById(10L))
                .thenReturn(Optional.of(healthPlan));

        when(patientMapper.toEntity(
                request,
                user,
                healthPlan
        )).thenReturn(patient);

        when(patientRepository.save(patient))
                .thenReturn(patient);

        Patient createdPatient =
                patientService.create(request);

        assertSame(patient, createdPatient);

        verify(userService)
                .findById(1L);

        verify(healthPlanService)
                .findById(10L);

        verify(patientMapper)
                .toEntity(
                        request,
                        user,
                        healthPlan
                );

        verify(patientRepository)
                .save(patient);
    }

    //PatientRequest --> userId null
    @Test
    void shouldRejectPatientWhenUserDoesNotExist() {

        PatientRepository patientRepository =
                mock(PatientRepository.class);

        UserService userService =
                mock(UserService.class);

        HealthPlanService healthPlanService =
                mock(HealthPlanService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientService patientService =
                new PatientService(
                        patientRepository,
                        userService,
                        healthPlanService,
                        patientMapper
                );

        PatientRequest request =
                new PatientRequest(
                        999L,
                        "12345678901",
                        LocalDate.of(1985, 7, 3),
                        "11999999999",
                        PaymentType.PRIVATE,
                        null
                );

        when(userService.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> patientService.create(request)
        );

        verify(patientMapper, never())
                .toEntity(
                        any(PatientRequest.class),
                        any(User.class),
                        any()
                );

        verify(patientRepository, never())
                .save(any(Patient.class));
    }

    // PatientRequest user found HealthPlan null -> Health plan not found.
    @Test
    void shouldRejectPatientWhenHealthPlanDoesNotExist() {

        PatientRepository patientRepository =
                mock(PatientRepository.class);

        UserService userService =
                mock(UserService.class);

        HealthPlanService healthPlanService =
                mock(HealthPlanService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientService patientService =
                new PatientService(
                        patientRepository,
                        userService,
                        healthPlanService,
                        patientMapper
                );

        PatientRequest request =
                new PatientRequest(
                        1L,
                        "12345678901",
                        LocalDate.of(1985, 7, 3),
                        "11999999999",
                        PaymentType.HEALTH_INSURANCE,
                        999L
                );

        User user = new User();
        user.setId(1L);

        when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        when(healthPlanService.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> patientService.create(request)
        );

        verify(userService)
                .findById(1L);

        verify(healthPlanService)
                .findById(999L);

        verify(patientMapper, never())
                .toEntity(
                        any(PatientRequest.class),
                        any(User.class),
                        any()
                );

        verify(patientRepository, never())
                .save(any(Patient.class));
    }

    @Test
    void shouldFindAllPatients() {

        // Arrange
        Patient patient1 = new Patient();
        Patient patient2 = new Patient();

        List<Patient> patients =
                List.of(patient1, patient2);

        PatientRepository patientRepository =
                mock(PatientRepository.class);

        UserService userService =
                mock(UserService.class);

        HealthPlanService healthPlanService =
                mock(HealthPlanService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientService patientService =
                new PatientService(
                        patientRepository,
                        userService,
                        healthPlanService,
                        patientMapper
                );

        when(patientRepository.findAll())
                .thenReturn(patients);

        // Act
        List<Patient> result =
                patientService.findAll();

        // Assert
        assertSame(patients, result);

        verify(patientRepository).findAll();
    }

    @Test
    void shouldFindPatientById() {

        // Arrange
        Long patientId = 100L;

        Patient patient = new Patient();

        PatientRepository patientRepository =
                mock(PatientRepository.class);

        UserService userService =
                mock(UserService.class);

        HealthPlanService healthPlanService =
                mock(HealthPlanService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientService patientService =
                new PatientService(
                        patientRepository,
                        userService,
                        healthPlanService,
                        patientMapper
                );

        when(patientRepository.findById(patientId))
                .thenReturn(Optional.of(patient));

        // Act
        Optional<Patient> result =
                patientService.findById(patientId);

        // Assert
        assertTrue(result.isPresent());
        assertSame(patient, result.get());

        verify(patientRepository).findById(patientId);
    }

    @Test
    void shouldReturnEmptyWhenPatientNotFound() {

        // Arrange
        Long patientId = 999L;

        PatientRepository patientRepository =
                mock(PatientRepository.class);

        UserService userService =
                mock(UserService.class);

        HealthPlanService healthPlanService =
                mock(HealthPlanService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientService patientService =
                new PatientService(
                        patientRepository,
                        userService,
                        healthPlanService,
                        patientMapper
                );

        when(patientRepository.findById(patientId))
                .thenReturn(Optional.empty());

        // Act
        Optional<Patient> result =
                patientService.findById(patientId);

        // Assert
        assertTrue(result.isEmpty());

        verify(patientRepository).findById(patientId);
    }

    @Test
    void shouldUpdatePatientPhone() {

        // Arrange
        Long patientId = 100L;
        String newPhone = "11988887777";

        Patient patient = new Patient();
        patient.setPhone("11999999999");

        PatientRepository patientRepository =
                mock(PatientRepository.class);

        UserService userService =
                mock(UserService.class);

        HealthPlanService healthPlanService =
                mock(HealthPlanService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientService patientService =
                new PatientService(
                        patientRepository,
                        userService,
                        healthPlanService,
                        patientMapper
                );

        when(patientRepository.findById(patientId))
                .thenReturn(Optional.of(patient));

        when(patientRepository.save(patient))
                .thenReturn(patient);

        // Act
        Patient result =
                patientService.updatePhone(patientId, newPhone);

        // Assert
        assertSame(patient, result);
        assertEquals(newPhone, result.getPhone());

        verify(patientRepository).findById(patientId);
        verify(patientRepository).save(patient);
    }

    @Test
    void shouldRejectPhoneUpdateWhenPatientNotFound() {

        // Arrange
        Long patientId = 999L;
        String newPhone = "11988887777";

        PatientRepository patientRepository =
                mock(PatientRepository.class);

        UserService userService =
                mock(UserService.class);

        HealthPlanService healthPlanService =
                mock(HealthPlanService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientService patientService =
                new PatientService(
                        patientRepository,
                        userService,
                        healthPlanService,
                        patientMapper
                );

        when(patientRepository.findById(patientId))
                .thenReturn(Optional.empty());

        // Act + Assert
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> patientService.updatePhone(
                                patientId,
                                newPhone
                        )
                );

        assertEquals("Patient not found.", exception.getMessage());

        verify(patientRepository).findById(patientId);
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void shouldUpdatePrivatePatient() {

        // Arrange
        Long patientId = 100L;

        Patient patient = new Patient();
        patient.setId(patientId);
        patient.setCpf("12345678901");
        patient.setBirthDate(LocalDate.of(1985, 7, 3));
        patient.setPhone("11999999999");
        patient.setPaymentType(PaymentType.PRIVATE);

        PatientUpdateRequest request =
                new PatientUpdateRequest(
                        "98765432100",
                        LocalDate.of(1985, 7, 3),
                        "11988887777",
                        PaymentType.PRIVATE,
                        null
                );

        PatientRepository patientRepository =
                mock(PatientRepository.class);

        UserService userService =
                mock(UserService.class);

        HealthPlanService healthPlanService =
                mock(HealthPlanService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientService patientService =
                new PatientService(
                        patientRepository,
                        userService,
                        healthPlanService,
                        patientMapper
                );

        when(patientRepository.findById(patientId))
                .thenReturn(Optional.of(patient));

        when(patientRepository.save(patient))
                .thenReturn(patient);

        // Act
        Patient result =
                patientService.update(patientId, request);

        // Assert
        assertSame(patient, result);
        assertEquals(patientId, result.getId());
        assertEquals(request.cpf(), result.getCpf());
        assertEquals(request.birthDate(), result.getBirthDate());
        assertEquals(request.phone(), result.getPhone());
        assertEquals(request.paymentType(), result.getPaymentType());
        assertNull(result.getHealthPlan());

        verify(patientRepository).findById(patientId);
        verify(patientRepository).save(patient);
    }

    @Test
    void shouldUpdatePatientWithHealthInsurance() {

        // Arrange
        Long patientId = 100L;
        Long healthPlanId = 5L;

        Patient patient = new Patient();
        patient.setId(patientId);
        patient.setPaymentType(PaymentType.PRIVATE);

        HealthPlan healthPlan = new HealthPlan();
        healthPlan.setId(healthPlanId);
        healthPlan.setActive(true);

        PatientUpdateRequest request =
                new PatientUpdateRequest(
                        "98765432100",
                        LocalDate.of(1985, 7, 3),
                        "11988887777",
                        PaymentType.HEALTH_INSURANCE,
                        healthPlanId
                );

        PatientRepository patientRepository =
                mock(PatientRepository.class);

        UserService userService =
                mock(UserService.class);

        HealthPlanService healthPlanService =
                mock(HealthPlanService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientService patientService =
                new PatientService(
                        patientRepository,
                        userService,
                        healthPlanService,
                        patientMapper
                );

        when(patientRepository.findById(patientId))
                .thenReturn(Optional.of(patient));

        when(healthPlanService.findById(healthPlanId))
                .thenReturn(Optional.of(healthPlan));

        when(patientRepository.save(patient))
                .thenReturn(patient);

        // Act
        Patient result = patientService.update(patientId, request);

        // Assert
        assertSame(patient, result);
        assertEquals(PaymentType.HEALTH_INSURANCE,
                result.getPaymentType());
        assertSame(healthPlan, result.getHealthPlan());

        verify(healthPlanService).findById(healthPlanId);
        verify(patientRepository).save(patient);
    }

    @Test
    void shouldRejectUpdateWhenHealthPlanNotFound() {

        // Arrange
        Long patientId = 100L;
        Long healthPlanId = 999L;

        Patient patient = new Patient();
        patient.setId(patientId);
        patient.setPaymentType(PaymentType.PRIVATE);

        PatientUpdateRequest request =
                new PatientUpdateRequest(
                        "98765432100",
                        LocalDate.of(1985, 7, 3),
                        "11988887777",
                        PaymentType.HEALTH_INSURANCE,
                        healthPlanId
                );

        PatientRepository patientRepository =
                mock(PatientRepository.class);

        UserService userService =
                mock(UserService.class);

        HealthPlanService healthPlanService =
                mock(HealthPlanService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientService patientService =
                new PatientService(
                        patientRepository,
                        userService,
                        healthPlanService,
                        patientMapper
                );

        when(patientRepository.findById(patientId))
                .thenReturn(Optional.of(patient));

        when(healthPlanService.findById(healthPlanId))
                .thenReturn(Optional.empty());

        // Act
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> patientService.update(patientId, request)
                );

        // Assert
        assertEquals(
                "Health plan not found.",
                exception.getMessage()
        );

        verify(patientRepository).findById(patientId);
        verify(healthPlanService).findById(healthPlanId);
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void shouldRejectUpdateWhenHealthPlanIsInactive() {

        // Arrange
        Long patientId = 100L;
        Long healthPlanId = 5L;

        Patient patient = new Patient();
        patient.setId(patientId);
        patient.setPaymentType(PaymentType.PRIVATE);

        HealthPlan healthPlan = new HealthPlan();
        healthPlan.setId(healthPlanId);
        healthPlan.setActive(false);

        PatientUpdateRequest request =
                new PatientUpdateRequest(
                        "98765432100",
                        LocalDate.of(1985, 7, 3),
                        "11988887777",
                        PaymentType.HEALTH_INSURANCE,
                        healthPlanId
                );

        PatientRepository patientRepository =
                mock(PatientRepository.class);

        UserService userService =
                mock(UserService.class);

        HealthPlanService healthPlanService =
                mock(HealthPlanService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientService patientService =
                new PatientService(
                        patientRepository,
                        userService,
                        healthPlanService,
                        patientMapper
                );

        when(patientRepository.findById(patientId))
                .thenReturn(Optional.of(patient));

        when(healthPlanService.findById(healthPlanId))
                .thenReturn(Optional.of(healthPlan));

        // Act
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> patientService.update(patientId, request)
                );

        // Assert
        assertEquals(
                "Health insurance patients must have an active health plan.",
                exception.getMessage()
        );

        verify(patientRepository).findById(patientId);
        verify(healthPlanService).findById(healthPlanId);
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void shouldRejectUpdateWhenPatientNotFound() {

        // Arrange
        Long patientId = 999L;

        PatientUpdateRequest request =
                new PatientUpdateRequest(
                        "98765432100",
                        LocalDate.of(1985, 7, 3),
                        "11988887777",
                        PaymentType.PRIVATE,
                        null
                );

        PatientRepository patientRepository =
                mock(PatientRepository.class);

        UserService userService =
                mock(UserService.class);

        HealthPlanService healthPlanService =
                mock(HealthPlanService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientService patientService =
                new PatientService(
                        patientRepository,
                        userService,
                        healthPlanService,
                        patientMapper
                );

        when(patientRepository.findById(patientId))
                .thenReturn(Optional.empty());

        // Act
        PatientNotFoundException exception = assertThrows(
                PatientNotFoundException.class,
                () -> patientService.update(patientId, request)
        );

        // Assert
        assertEquals(
                "Patient not found.",
                exception.getMessage()
        );

        verify(patientRepository).findById(patientId);

        verify(patientRepository, never())
                .save(any(Patient.class));

        verifyNoInteractions(healthPlanService);
    }

    @Test
    void shouldDeletePatientWhenPatientExists() {

        // Arrange
        Long patientId = 100L;

        Patient patient = new Patient();
        patient.setId(patientId);

        PatientRepository patientRepository =
                mock(PatientRepository.class);

        UserService userService =
                mock(UserService.class);

        HealthPlanService healthPlanService =
                mock(HealthPlanService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientService patientService =
                new PatientService(
                        patientRepository,
                        userService,
                        healthPlanService,
                        patientMapper
                );

        when(patientRepository.findById(patientId))
                .thenReturn(Optional.of(patient));

        // Act
        patientService.delete(patientId);

        // Assert
        verify(patientRepository).findById(patientId);
        verify(patientRepository).delete(patient);
    }

    @Test
    void shouldRejectDeleteWhenPatientNotFound() {

        // Arrange
        Long patientId = 999L;

        PatientRepository patientRepository =
                mock(PatientRepository.class);

        UserService userService =
                mock(UserService.class);

        HealthPlanService healthPlanService =
                mock(HealthPlanService.class);

        PatientMapper patientMapper =
                mock(PatientMapper.class);

        PatientService patientService =
                new PatientService(
                        patientRepository,
                        userService,
                        healthPlanService,
                        patientMapper
                );

        when(patientRepository.findById(patientId))
                .thenReturn(Optional.empty());

        // Act & Assert
        PatientNotFoundException exception = assertThrows(
                PatientNotFoundException.class,
                () -> patientService.delete(patientId)
        );

        assertEquals("Patient not found.", exception.getMessage());

        verify(patientRepository).findById(patientId);
        verify(patientRepository, never()).delete(any(Patient.class));
    }
}
