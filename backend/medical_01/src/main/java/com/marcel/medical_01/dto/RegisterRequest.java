package com.marcel.medical_01.dto;

public record RegisterRequest(
        String name,
        String email,
        String password
) {
}
