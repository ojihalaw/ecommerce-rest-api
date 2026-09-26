package com.example.ecommerce.product;

import com.example.ecommerce.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProductResponse> create(
            @Valid @RequestBody CreateProductRequest request
    ) {
        ProductResponse response = productService.create(request);

        return ApiResponse.success(
                201,
                "Product added successfully",
                response
        );
    }

    @GetMapping
    public ApiResponse<List<ProductResponse>> findAll() {

        List<ProductResponse> responses = productService.findAll();

        return ApiResponse.success(
                200,
                "Product retrieve successfully",
                responses
        );
    }
}