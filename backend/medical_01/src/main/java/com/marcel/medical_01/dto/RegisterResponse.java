package com.marcel.medical_01.dto;

import com.marcel.medical_01.model.Role;

public record RegisterResponse(
        Long id,
        String name,
        String email,
        Role role
) {
}
