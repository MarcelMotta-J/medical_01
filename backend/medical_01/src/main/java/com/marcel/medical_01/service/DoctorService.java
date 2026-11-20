package com.marcel.medical_01.service;

import com.marcel.medical_01.model.Doctor;
import com.marcel.medical_01.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import java.util.List;

import com.marcel.medical_01.dto.doctor.DoctorRequest;
import com.marcel.medical_01.model.User;

import com.marcel.medical_01.model.Role;

import com.marcel.medical_01.dto.doctor.DoctorUpdateRequest;

import com.marcel.medical_01.exception.DoctorNotFoundException;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserService userService;

    public DoctorService(DoctorRepository doctorRepository, UserService userService) {
        this.doctorRepository = doctorRepository;
        this.userService = userService;
    }

    public Optional<Doctor> findById(Long id) {

        return doctorRepository.findById(id);
    }

    public List<Doctor> findAll() {

        return doctorRepository.findAll();
    }

    public User findUserFromRequest(DoctorRequest request) {

        User user = userService.findById(request.userId())
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found.")
                );

        if (user.getRole() != Role.DOCTOR) {
            throw new IllegalArgumentException(
                    "User must have DOCTOR role."
            );
        }

        return user;
    }

    public Doctor create(DoctorRequest request) {

        // 1. Find and validate the user
        User user = findUserFromRequest(request);

        // 2. Create the doctor entity
        Doctor doctor = new Doctor();

        // 3. Associate the user and CRM information
        doctor.setUser(user);
        doctor.setCrm(request.crm());
        doctor.setCrmUf(request.crmUf());

        // 4. Save the doctor
        return doctorRepository.save(doctor);
    }

    public Doctor update(Long id, DoctorUpdateRequest request) {

        // 1. Find the existing doctor
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(DoctorNotFoundException::new);

        // 2. Update CRM information
        doctor.setCrm(request.crm());
        doctor.setCrmUf(request.crmUf());

        // 3. Save the updated doctor
        return doctorRepository.save(doctor);
    }

    public void deactivate(Long id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(DoctorNotFoundException::new);

        doctor.setActive(false);

        doctorRepository.save(doctor);
    }

    public void activate(Long id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(DoctorNotFoundException::new);

        doctor.setActive(true);

        doctorRepository.save(doctor);
    }

}
