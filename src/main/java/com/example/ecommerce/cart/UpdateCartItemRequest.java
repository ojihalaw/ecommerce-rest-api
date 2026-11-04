package com.example.ecommerce.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateCartItemRequest(

        @NotNull
        @Min(1)
        Integer quantity
) {
}