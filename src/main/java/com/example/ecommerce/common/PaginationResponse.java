package com.example.ecommerce.common;

public record PaginationResponse(
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
