
package com.marcel.medical_01.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpecialtyTest {

    @Test
    void shouldSetAndGetSpecialtyName() {

        // Arrange
        Specialty specialty = new Specialty();
        String name = "Cardiologia";

        // Act
        specialty.setName(name);

        // Assert
        assertEquals(name, specialty.getName());
    }


    @Test
    void shouldSetAndGetSpecialtyId() {

        // Arrange
        Specialty specialty = new Specialty();
        Long id = 1L;

        // Act
        specialty.setId(id);

        // Assert
        assertEquals(id, specialty.getId());
    }

}
