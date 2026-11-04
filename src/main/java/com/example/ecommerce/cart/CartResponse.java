package com.example.ecommerce.cart;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartResponse(
        UUID id,
        List<CartItemResponse> items,
        BigDecimal total
) {

    public record CartItemResponse(
            UUID productId,
            String productName,
            BigDecimal price,
            Integer quantity,
            BigDecimal subtotal
    ) {
    }
}