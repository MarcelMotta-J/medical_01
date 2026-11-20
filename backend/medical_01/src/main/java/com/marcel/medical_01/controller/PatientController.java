package com.marcel.medical_01.controller;

import com.marcel.medical_01.dto.patient.PatientRequest;
import com.marcel.medical_01.dto.patient.PatientResponse;
import com.marcel.medical_01.mapper.PatientMapper;
import com.marcel.medical_01.model.Patient;
import com.marcel.medical_01.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;

import java.util.List;

import com.marcel.medical_01.dto.patient.PatientUpdateRequest;

import com.marcel.medical_01.exception.PatientNotFoundException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;
    private final PatientMapper patientMapper;

    public PatientController(
            PatientService patientService,
            PatientMapper patientMapper
    ) {
        this.patientService = patientService;
        this.patientMapper = patientMapper;
    }

    @PostMapping
    public ResponseEntity<PatientResponse> create(
            @Valid @RequestBody PatientRequest request
    ) {

        Patient patient =
                patientService.create(request);

        PatientResponse response =
                patientMapper.toResponse(patient);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<PatientResponse>> findAll() {

        List<PatientResponse> responses =
                patientService.findAll()
                        .stream()
                        .map(patientMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> findById(
            @PathVariable Long id) {

        return patientService.findById(id)
                .map(patientMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PatientUpdateRequest request) {

        Patient patient = patientService.update(id, request);

        PatientResponse response = patientMapper.toResponse(patient);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        patientService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
