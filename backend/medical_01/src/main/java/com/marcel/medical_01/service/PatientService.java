package com.marcel.medical_01.service;

import com.marcel.medical_01.model.Patient;
import com.marcel.medical_01.model.PaymentType;
import com.marcel.medical_01.repository.PatientRepository;
import org.springframework.stereotype.Service;

import com.marcel.medical_01.dto.patient.PatientRequest;
import com.marcel.medical_01.model.User;

import com.marcel.medical_01.model.HealthPlan;

import com.marcel.medical_01.mapper.PatientMapper;

import java.util.List;

import java.util.Optional;

import com.marcel.medical_01.dto.patient.PatientUpdateRequest;

import com.marcel.medical_01.exception.PatientNotFoundException;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserService userService;
    private final HealthPlanService healthPlanService;
    private final PatientMapper patientMapper;

    public PatientService(PatientRepository patientRepository, UserService userService, HealthPlanService healthPlanService, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.userService = userService;
        this.healthPlanService = healthPlanService;
        this.patientMapper = patientMapper;
    }

    public Patient save(Patient patient) {

        validatePayment(patient);

        return patientRepository.save(patient);
    }

    private void validatePayment(Patient patient) {
        validatePayment(
                patient.getPaymentType(),
                patient.getHealthPlan()
        );
    }

    private void validatePayment(
            PaymentType paymentType,
            HealthPlan healthPlan
    ) {
        if (paymentType == PaymentType.PRIVATE
                && healthPlan != null) {
            throw new IllegalArgumentException(
                    "Private patients cannot have a health plan.");
        }

        if (paymentType == PaymentType.HEALTH_INSURANCE
                && healthPlan == null) {
            throw new IllegalArgumentException(
                    "Health insurance patients must have a health plan.");
        }

        if (paymentType == PaymentType.HEALTH_INSURANCE
                && !healthPlan.isActive()) {
            throw new IllegalArgumentException(
                    "Health insurance patients must have an active health plan.");
        }
    }

    public User findUserFromRequest(PatientRequest request) {

        return userService.findById(request.userId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found."
                        )
                );
    }

    public HealthPlan findHealthPlanFromRequest(PatientRequest request) {

        if (request.healthPlanId() == null) {
            return null;
        }

        return healthPlanService.findById(request.healthPlanId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Health plan not found."
                        )
                );
    }

    public Patient create(PatientRequest request) {

        User user = findUserFromRequest(request);

        HealthPlan healthPlan =
                findHealthPlanFromRequest(request);

        Patient patient =
                patientMapper.toEntity(
                        request,
                        user,
                        healthPlan
                );

        return save(patient);
    }

    public List<Patient> findAll() {

        return patientRepository.findAll();
    }

    public Optional<Patient> findById(Long id) {

        return patientRepository.findById(id);
    }

    public Patient updatePhone(Long id, String newPhone) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Patient not found."
                        )
                );

        patient.setPhone(newPhone);

        return patientRepository.save(patient);
    }

    public Patient update(Long id, PatientUpdateRequest request) {

        // 1. Find the existing patient
        Patient patient = patientRepository.findById(id)
                .orElseThrow(PatientNotFoundException::new);
        // or   .orElseThrow(() -> new PatientNotFoundException());

        // 2. Resolve the requested health plan
        HealthPlan healthPlan = null;

        if (request.healthPlanId() != null) {
            healthPlan = healthPlanService.findById(request.healthPlanId())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Health plan not found.")
                    );
        }

        // 3. Validate before modifying the patient
        validatePayment(request.paymentType(), healthPlan);

        // 4. Apply the changes
        patient.setCpf(request.cpf());
        patient.setBirthDate(request.birthDate());
        patient.setPhone(request.phone());
        patient.setPaymentType(request.paymentType());
        patient.setHealthPlan(healthPlan);

        // 5. Save the updated patient
        return save(patient);
    }

    public void delete(Long id) {

        // Find the patient
        Patient patient = patientRepository.findById(id)
                .orElseThrow(PatientNotFoundException::new);

        // Delete the patient
        patientRepository.delete(patient);
    }

}
