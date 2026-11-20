package com.marcel.medical_01.dto.patient;

import com.marcel.medical_01.model.PaymentType;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PatientUpdateRequestValidationTest {

    @Test
    void shouldRejectBlankCpf() {

        // Arrange
        PatientUpdateRequest request = new PatientUpdateRequest(
                "",
                LocalDate.of(1990, 5, 15),
                "11999999999",
                PaymentType.PRIVATE,
                null
        );

        // Act
        try (ValidatorFactory factory =
                     Validation.buildDefaultValidatorFactory()) {

            Validator validator = factory.getValidator();

            var violations = validator.validate(request);

            // Assert
            assertTrue(
                    violations.stream()
                            .anyMatch(violation ->
                                    violation.getPropertyPath()
                                            .toString()
                                            .equals("cpf")
                            )
            );
        }
    }

    @Test
    void shouldRejectCpfContainingOnlySpaces() {

        // Arrange
        PatientUpdateRequest request = new PatientUpdateRequest(
                "   ",
                LocalDate.of(1990, 5, 15),
                "11999999999",
                PaymentType.PRIVATE,
                null
        );

        // Act
        try (ValidatorFactory factory =
                     Validation.buildDefaultValidatorFactory()) {

            Validator validator = factory.getValidator();

            var violations = validator.validate(request);

            // Assert
            assertTrue(
                    violations.stream()
                            .anyMatch(violation ->
                                    violation.getPropertyPath()
                                            .toString()
                                            .equals("cpf")
                            )
            );
        }
    }

    @Test
    void shouldRejectFutureBirthDate() {

        // Arrange
        PatientUpdateRequest request = new PatientUpdateRequest(
                "12345678901",
                LocalDate.now().plusDays(1),
                "11999999999",
                PaymentType.PRIVATE,
                null
        );

        // Act
        try (ValidatorFactory factory =
                     Validation.buildDefaultValidatorFactory()) {

            Validator validator = factory.getValidator();

            var violations = validator.validate(request);

            // Assert
            assertTrue(
                    violations.stream()
                            .anyMatch(violation ->
                                    violation.getPropertyPath()
                                            .toString()
                                            .equals("birthDate")
                            )
            );
        }
    }

    @Test
    void shouldRejectNullBirthDate() {

        // Arrange
        PatientUpdateRequest request = new PatientUpdateRequest(
                "12345678901",
                null,
                "11999999999",
                PaymentType.PRIVATE,
                null
        );

        // Act
        try (ValidatorFactory factory =
                     Validation.buildDefaultValidatorFactory()) {

            Validator validator = factory.getValidator();

            var violations = validator.validate(request);

            // Assert
            assertTrue(
                    violations.stream()
                            .anyMatch(violation ->
                                    violation.getPropertyPath()
                                            .toString()
                                            .equals("birthDate")
                            )
            );
        }
    }

    @Test
    void shouldRejectBlankPhone() {

        // Arrange
        PatientUpdateRequest request = new PatientUpdateRequest(
                "12345678901",
                LocalDate.of(1990, 5, 15),
                "",
                PaymentType.PRIVATE,
                null
        );

        // Act
        try (ValidatorFactory factory =
                     Validation.buildDefaultValidatorFactory()) {

            Validator validator = factory.getValidator();

            var violations = validator.validate(request);

            // Assert
            assertTrue(
                    violations.stream()
                            .anyMatch(violation ->
                                    violation.getPropertyPath()
                                            .toString()
                                            .equals("phone")
                            )
            );
        }
    }

    @Test
    void shouldRejectNullPaymentType() {

        // Arrange
        PatientUpdateRequest request = new PatientUpdateRequest(
                "12345678901",
                LocalDate.of(1990, 5, 15),
                "11999999999",
                null,
                null
        );

        // Act
        try (ValidatorFactory factory =
                     Validation.buildDefaultValidatorFactory()) {

            Validator validator = factory.getValidator();

            var violations = validator.validate(request);

            // Assert
            assertTrue(
                    violations.stream()
                            .anyMatch(violation ->
                                    violation.getPropertyPath()
                                            .toString()
                                            .equals("paymentType")
                            )
            );
        }
    }
}
