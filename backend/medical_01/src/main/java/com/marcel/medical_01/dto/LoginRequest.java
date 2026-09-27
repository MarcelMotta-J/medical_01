package com.marcel.medical_01.dto;

public record LoginRequest(
        String email,
        String password
) {
}
