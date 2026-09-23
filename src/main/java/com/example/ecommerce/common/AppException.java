package com.example.ecommerce.common;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private final int status;

    public AppException(int status, String message) {
        super(message);
        this.status = status;
    }
}
