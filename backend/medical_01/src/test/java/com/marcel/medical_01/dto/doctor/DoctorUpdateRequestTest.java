package com.marcel.medical_01.dto.doctor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DoctorUpdateRequestTest {

    @Test
    void shouldCreateValidDoctorUpdateRequest() {

        // Arrange
        DoctorUpdateRequest request =
                new DoctorUpdateRequest("987654", "RJ");

        // Assert
        assertEquals("987654", request.crm());
        assertEquals("RJ", request.crmUf());

        System.out.println(
                "[DoctorUpdateRequestTest] Valid update => CRM: "
                        + request.crm()
                        + " | UF: "
                        + request.crmUf()
        );
    }

    @Test
    void shouldRejectBlankCrm() {

        // Arrange
        DoctorUpdateRequest request =
                new DoctorUpdateRequest("", "SP");

        Validator validator = Validation
                .buildDefaultValidatorFactory()
                .getValidator();

        // Act
        Set<ConstraintViolation<DoctorUpdateRequest>> violations =
                validator.validate(request);

        // Assert
        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath()
                                        .toString()
                                        .equals("crm"))
        );

        violations.forEach(violation ->
                System.out.println(
                        "[DoctorUpdateRequestTest] Invalid field: "
                                + violation.getPropertyPath()
                                + " | Message: "
                                + violation.getMessage()
                )
        );
    }

    @Test
    void shouldRejectCrmLongerThan20Characters() {

        // Arrange
        DoctorUpdateRequest request =
                new DoctorUpdateRequest("123456789012345678901", "SP");

        Validator validator = Validation
                .buildDefaultValidatorFactory()
                .getValidator();

        // Act
        Set<ConstraintViolation<DoctorUpdateRequest>> violations =
                validator.validate(request);

        // Assert
        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath()
                                        .toString()
                                        .equals("crm"))
        );

        violations.forEach(violation ->
                System.out.println(
                        "[DoctorUpdateRequestTest] Invalid field: "
                                + violation.getPropertyPath()
                                + " | Message: "
                                + violation.getMessage()
                )
        );
    }

    @Test
    void shouldRejectLowercaseCrmUf() {

        // Arrange
        DoctorUpdateRequest request =
                new DoctorUpdateRequest("987654", "sp");

        Validator validator = Validation
                .buildDefaultValidatorFactory()
                .getValidator();

        // Act
        Set<ConstraintViolation<DoctorUpdateRequest>> violations =
                validator.validate(request);

        // Assert
        assertTrue(
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath()
                                        .toString()
                                        .equals("crmUf"))
        );

        violations.forEach(violation ->
                System.out.println(
                        "[DoctorUpdateRequestTest] Invalid field: "
                                + violation.getPropertyPath()
                                + " | Message: "
                                + violation.getMessage()
                )
        );
    }


}
