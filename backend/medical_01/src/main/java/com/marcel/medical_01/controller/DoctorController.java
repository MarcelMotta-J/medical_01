package com.marcel.medical_01.controller;

import com.marcel.medical_01.dto.doctor.DoctorRequest;
import com.marcel.medical_01.dto.doctor.DoctorResponse;
import com.marcel.medical_01.mapper.DoctorMapper;
import com.marcel.medical_01.model.Doctor;
import com.marcel.medical_01.service.DoctorService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.marcel.medical_01.dto.doctor.DoctorUpdateRequest;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<DoctorResponse> create(
            @Valid @RequestBody DoctorRequest request) {

        // 1. Create doctor
        Doctor doctor = doctorService.create(request);

        // 2. Convert Entity to DTO
        DoctorResponse response =
                DoctorMapper.toResponse(doctor);

        // 3. Return HTTP 201 Created
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> findAll() {

        List<DoctorResponse> responses =
                doctorService.findAll()
                        .stream()
                        .map(DoctorMapper::toResponse)
                        // or
                        // .map(doctor -> DoctorMapper.toResponse(doctor))
                        .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> findById(
            @PathVariable Long id) {

        return doctorService.findById(id)
                .map(DoctorMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody DoctorUpdateRequest request) {

        // 1. Update doctor
        Doctor doctor = doctorService.update(id, request);

        // 2. Convert Entity to DTO
        DoctorResponse response =
                DoctorMapper.toResponse(doctor);

        // 3. Return HTTP 200 OK
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {

        doctorService.deactivate(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {

        doctorService.activate(id);

        return ResponseEntity.noContent().build();
    }
}