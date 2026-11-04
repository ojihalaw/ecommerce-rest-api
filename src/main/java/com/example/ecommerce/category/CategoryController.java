package com.example.ecommerce.category;

import com.example.ecommerce.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CategoryResponse> create(
            @Valid @RequestBody CreateCategoryRequest request
    ){
        CategoryResponse response = categoryService.create(request);

        return ApiResponse.success(
                201,
                "Category added successfully",
                response
        );
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<CategoryResponse>> findAll(){
        List<CategoryResponse> response = categoryService.findAll();

        return ApiResponse.success(
                200,
                "Categories retrieved successfully",
                response
        );
    };
}
