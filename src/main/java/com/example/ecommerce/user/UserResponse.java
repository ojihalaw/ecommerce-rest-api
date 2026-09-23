package com.example.ecommerce.user;

import java.util.UUID;

public record UserResponse(
    UUID id,
    String username,
    String email,
    Role role,
    UserStatus status
) {
}
