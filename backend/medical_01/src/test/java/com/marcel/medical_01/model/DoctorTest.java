package com.marcel.medical_01.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertTrue;


class DoctorTest {

    @Test
    void shouldAssociateDoctorWithUser() {

        // Arrange
        User user = new User();
        Doctor doctor = new Doctor();

        // Act
        doctor.setUser(user);

        // Assert
        assertSame(user, doctor.getUser());
    }

    @Test
    void shouldSetAndGetDoctorId() {

        // Arrange
        Doctor doctor = new Doctor();
        Long id = 1L;

        // Act
        doctor.setId(id);

        // Assert
        assertEquals(id, doctor.getId());
    }

    @Test
    void shouldSetAndGetDoctorCrm() {

        // Arrange
        Doctor doctor = new Doctor();
        String crm = "123456";

        // Act
        doctor.setCrm(crm);

        // Assert
        assertEquals(crm, doctor.getCrm());
    }

    @Test
    void shouldSetAndGetDoctorCrmUf() {

        // Arrange
        Doctor doctor = new Doctor();
        String crmUf = "SP";

        // Act
        doctor.setCrmUf(crmUf);

        // Assert
        assertEquals(crmUf, doctor.getCrmUf());
    }

    @Test
    void shouldAssociateDoctorWithMultipleSpecialties() {

        // Arrange
        Doctor doctor = new Doctor();

        Specialty cardiology = new Specialty();
        cardiology.setName("Cardiologia");

        Specialty generalMedicine = new Specialty();
        generalMedicine.setName("Clínica Médica");

        // Act
        doctor.getSpecialties().add(cardiology);
        doctor.getSpecialties().add(generalMedicine);

        // Assert
        assertEquals(2, doctor.getSpecialties().size());

        assertTrue(doctor.getSpecialties().contains(cardiology));
        assertTrue(doctor.getSpecialties().contains(generalMedicine));
    }
}
