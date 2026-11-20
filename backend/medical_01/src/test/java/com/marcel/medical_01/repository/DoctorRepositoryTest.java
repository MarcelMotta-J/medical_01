package com.marcel.medical_01.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.marcel.medical_01.model.User;
import com.marcel.medical_01.model.Role;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.marcel.medical_01.model.Doctor;

import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.marcel.medical_01.model.Specialty;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.persistence.EntityManager;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveDoctorWithUser() {

        User user = new User();

        user.setName("Doctor Test One");
        user.setEmail("doctor1@test.com");
        user.setPassword("test-password");
        user.setRole(Role.DOCTOR);

        User savedUser = userRepository.saveAndFlush(user);

        assertNotNull(savedUser.getId());

        Doctor doctor = new Doctor();

        doctor.setUser(savedUser);
        doctor.setCrm("123456");
        doctor.setCrmUf("SP");

        Doctor savedDoctor = doctorRepository.saveAndFlush(doctor);

        assertNotNull(savedDoctor.getId());
    }

    @Test
    void shouldRejectDuplicateCrmAndUf() {

        User user1 = new User();
        user1.setName("Doctor One");
        user1.setEmail("doctor.one@test.com");
        user1.setPassword("test-password");
        user1.setRole(Role.DOCTOR);

        User savedUser1 = userRepository.saveAndFlush(user1);

        User user2 = new User();
        user2.setName("Doctor Two");
        user2.setEmail("doctor.two@test.com");
        user2.setPassword("test-password");
        user2.setRole(Role.DOCTOR);

        User savedUser2 = userRepository.saveAndFlush(user2);

        assertNotNull(savedUser1.getId());
        assertNotNull(savedUser2.getId());

        Doctor doctor1 = new Doctor();

        doctor1.setUser(savedUser1);
        doctor1.setCrm("123456");
        doctor1.setCrmUf("SP");

        Doctor savedDoctor1 = doctorRepository.saveAndFlush(doctor1);

        assertNotNull(savedDoctor1.getId());

        Doctor doctor2 = new Doctor();

        doctor2.setUser(savedUser2);
        doctor2.setCrm("123456");
        doctor2.setCrmUf("SP");

        assertThrows(
                DataIntegrityViolationException.class,
                () -> doctorRepository.saveAndFlush(doctor2)
        );



    }


    @Test
    void shouldSaveDoctorWithMultipleSpecialties() {

        // Arrange
        User user = new User();
        user.setName("Doctor With Specialties");
        user.setEmail("doctor.specialties@test.com");
        user.setPassword("test-password");
        user.setRole(Role.DOCTOR);

        User savedUser = userRepository.saveAndFlush(user);

        Specialty cardiology = new Specialty();
        cardiology.setName("Cardiologia");

        Specialty generalMedicine = new Specialty();
        generalMedicine.setName("Clínica Médica");

        Specialty savedCardiology =
                specialtyRepository.saveAndFlush(cardiology);

        Specialty savedGeneralMedicine =
                specialtyRepository.saveAndFlush(generalMedicine);

        Doctor doctor = new Doctor();
        doctor.setUser(savedUser);
        doctor.setCrm("987654");
        doctor.setCrmUf("SP");

        doctor.getSpecialties().add(savedCardiology);
        doctor.getSpecialties().add(savedGeneralMedicine);

        // Act
        Doctor savedDoctor = doctorRepository.saveAndFlush(doctor);

        Long doctorId = savedDoctor.getId();

        entityManager.clear();

        Doctor foundDoctor = doctorRepository.findById(doctorId)
                .orElseThrow();

        // Assert
        assertEquals(2, foundDoctor.getSpecialties().size());
    }


}
