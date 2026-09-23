package com.example.ecommerce.auth;

public record LoginResponse(
        String accessToken,
        String tokenType
) {
}
