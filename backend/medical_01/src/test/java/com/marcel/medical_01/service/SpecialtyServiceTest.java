
package com.marcel.medical_01.service;

import com.marcel.medical_01.model.Specialty;
import com.marcel.medical_01.repository.SpecialtyRepository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpecialtyServiceTest {

    @Test
    void shouldSaveSpecialty() {

        // Arrange
        SpecialtyRepository specialtyRepository =
                mock(SpecialtyRepository.class);

        SpecialtyService specialtyService =
                new SpecialtyService(specialtyRepository);

        Specialty specialty = new Specialty();
        specialty.setName("Cardiologia");

        when(specialtyRepository.save(specialty))
                .thenReturn(specialty);

        // Act
        Specialty savedSpecialty =
                specialtyService.save(specialty);

        // Assert
        assertSame(specialty, savedSpecialty);
    }


    @Test
    void shouldRejectSpecialtyWhenNameAlreadyExists() {

        // Arrange
        SpecialtyRepository specialtyRepository =
                mock(SpecialtyRepository.class);

        SpecialtyService specialtyService =
                new SpecialtyService(specialtyRepository);

        Specialty specialty = new Specialty();
        specialty.setName("Cardiologia");

        when(specialtyRepository.existsByName("Cardiologia"))
                .thenReturn(true);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> specialtyService.save(specialty)
        );

        verify(specialtyRepository, never())
                .save(specialty);
    }


    @Test
    void shouldFindSpecialtyById() {

        // Arrange
        SpecialtyRepository specialtyRepository =
                mock(SpecialtyRepository.class);

        SpecialtyService specialtyService =
                new SpecialtyService(specialtyRepository);

        Specialty specialty = new Specialty();
        specialty.setName("Cardiologia");

        when(specialtyRepository.findById(1L))
                .thenReturn(Optional.of(specialty));

        // Act
        Optional<Specialty> result =
                specialtyService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertSame(specialty, result.get());
    }


    @Test
    void shouldReturnEmptyWhenSpecialtyDoesNotExist() {

        // Arrange
        SpecialtyRepository specialtyRepository =
                mock(SpecialtyRepository.class);

        SpecialtyService specialtyService =
                new SpecialtyService(specialtyRepository);

        when(specialtyRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act
        Optional<Specialty> result =
                specialtyService.findById(999L);

        // Assert
        assertTrue(result.isEmpty());
    }


}
