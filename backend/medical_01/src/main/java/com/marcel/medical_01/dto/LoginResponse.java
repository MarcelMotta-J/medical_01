package com.marcel.medical_01.dto;

public record LoginResponse(
        String token,
        String email
) {
}
