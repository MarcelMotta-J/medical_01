package com.marcel.medical_01.dto.doctor;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DoctorRequestTest {

    @Test
    void shouldAcceptValidDoctorRequest() {

        DoctorRequest request =
                new DoctorRequest(10L, "123456", "SP");

        try (ValidatorFactory factory =
                     Validation.buildDefaultValidatorFactory()) {

            Validator validator = factory.getValidator();

            var violations = validator.validate(request);

            assertTrue(violations.isEmpty());

            System.out.println(
                    "[DoctorRequestTest] Valid doctor request => "
                            + "CRM: " + request.crm()
                            + " | UF: " + request.crmUf()
            );
        }
    }

    @Test
    void shouldRejectBlankCrm() {

        // 1. Create a request with a blank CRM
        DoctorRequest request =
                new DoctorRequest(10L, "", "SP");

        // 2. Create the validator
        try (ValidatorFactory factory =
                     Validation.buildDefaultValidatorFactory()) {

            Validator validator = factory.getValidator();

            // 3. Validate the request
            var violations = validator.validate(request);

            // 4. Verify that validation failed
            assertTrue(violations.stream()
                    .anyMatch(violation ->
                            violation.getPropertyPath()
                                    .toString().equals("crm")
                    ));

            // 5. Print validation messages
            violations.forEach(violation ->
                    System.out.println(
                            "[DoctorRequestTest] Invalid field: "
                                    + violation.getPropertyPath()
                                    + " | Message: "
                                    + violation.getMessage()
                    )
            );
        }
    }

    @Test
    void shouldRejectLowercaseCrmUf() {

        // 1. Create a request with lowercase UF
        DoctorRequest request =
                new DoctorRequest(10L, "123456", "sp");

        // 2. Create the validator
        try (ValidatorFactory factory =
                     Validation.buildDefaultValidatorFactory()) {

            Validator validator = factory.getValidator();

            // 3. Validate the request
            var violations = validator.validate(request);

            // 4. Verify that crmUf was rejected
            assertTrue(violations.stream()
                    .anyMatch(violation ->
                            violation.getPropertyPath()
                                    .toString().equals("crmUf")
                    ));

            // 5. Print validation messages
            violations.forEach(violation ->
                    System.out.println(
                            "[DoctorRequestTest] Invalid field: "
                                    + violation.getPropertyPath()
                                    + " | Message: "
                                    + violation.getMessage()
                    )
            );
        }
    }

    @Test
    void shouldRejectCrmUfWithThreeLetters() {

        // 1. Create a request with three letters
        DoctorRequest request =
                new DoctorRequest(10L, "123456", "SPP");

        // 2. Create the validator
        try (ValidatorFactory factory =
                     Validation.buildDefaultValidatorFactory()) {

            Validator validator = factory.getValidator();

            // 3. Validate the request
            var violations = validator.validate(request);

            // 4. Verify that crmUf was rejected
            assertTrue(violations.stream()
                    .anyMatch(violation ->
                            violation.getPropertyPath()
                                    .toString().equals("crmUf")
                    ));

            // 5. Print validation messages
            violations.forEach(violation ->
                    System.out.println(
                            "[DoctorRequestTest] Invalid field: "
                                    + violation.getPropertyPath()
                                    + " | Message: "
                                    + violation.getMessage()
                    )
            );
        }
    }

    @Test
    void shouldRejectNullUserId() {

        // 1. Create a request without userId
        DoctorRequest request =
                new DoctorRequest(null, "123456", "SP");

        // 2. Create the validator
        try (ValidatorFactory factory =
                     Validation.buildDefaultValidatorFactory()) {

            Validator validator = factory.getValidator();

            // 3. Validate the request
            var violations = validator.validate(request);

            // 4. Verify that userId was rejected
            assertTrue(violations.stream()
                    .anyMatch(violation ->
                            violation.getPropertyPath()
                                    .toString().equals("userId")
                    ));

            // 5. Print validation messages
            violations.forEach(violation ->
                    System.out.println(
                            "[DoctorRequestTest] Invalid field: "
                                    + violation.getPropertyPath()
                                    + " | Message: "
                                    + violation.getMessage()
                    )
            );
        }
    }

    @Test
    void shouldRejectCrmLongerThan20Characters() {

        // 1. Create a request with 21 characters in CRM
        DoctorRequest request =
                new DoctorRequest(10L, "123456789012345678901", "SP");

        // 2. Create the validator
        try (ValidatorFactory factory =
                     Validation.buildDefaultValidatorFactory()) {

            Validator validator = factory.getValidator();

            // 3. Validate the request
            var violations = validator.validate(request);

            // 4. Verify that CRM size was rejected
            assertTrue(violations.stream()
                    .anyMatch(violation ->
                            violation.getPropertyPath()
                                    .toString().equals("crm")
                    ));

            // 5. Print validation messages
            violations.forEach(violation ->
                    System.out.println(
                            "[DoctorRequestTest] Invalid field: "
                                    + violation.getPropertyPath()
                                    + " | Message: "
                                    + violation.getMessage()
                    )
            );
        }
    }
}
