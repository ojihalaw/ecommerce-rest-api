package com.example.ecommerce.common;

public record ApiResponse<T>(
        boolean success,
        int status,
        String message,
        T data,
        PaginationResponse pagination
) {

    public static <T> ApiResponse<T> success(
            int status,
            String message,
            T data
    ) {
        return new ApiResponse<>(
                true,
                status,
                message,
                data,
                null
        );
    }

    public static <T> ApiResponse<T> success(
            int status,
            String message,
            T data,
            PaginationResponse pagination
    ) {
        return new ApiResponse<>(
                true,
                status,
                message,
                data,
                pagination
        );
    }

    public static <T> ApiResponse<T> error(
            int status,
            String message,
            T data
    ) {
        return new ApiResponse<>(
                false,
                status,
                message,
                data,
                null
        );
    }
}