package com.example.ecommerce.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateProductRequest(

    @NotBlank
    @Size(max = 150)
    String name,

    String description,

    @NotNull
    @DecimalMin(value = "0.00")
    BigDecimal price,

    @NotNull
    @Min(0)
    Integer stock,

    @NotNull
    UUID categoryId

) {}