package com.example.ecommerce.cart;

import com.example.ecommerce.common.ApiResponse;
import com.example.ecommerce.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<CartResponse> addItem(
            Authentication authentication,
            @Valid @RequestBody AddCartItemRequest request
    ) {

        User user = (User) authentication.getPrincipal();

        CartResponse response =
                cartService.addItem(
                        user.getId(),
                        request
                );

        return ApiResponse.success(
                200,
                "Item added to cart successfully",
                response
        );
    }

    @GetMapping
    public ApiResponse<CartResponse> getCart(
            Authentication authentication
    ) {

        User user = (User) authentication.getPrincipal();

        CartResponse response =
                cartService.getCart(user.getId());

        return ApiResponse.success(
                200,
                "Cart retrieved successfully",
                response
        );
    }

    @PutMapping("/items/{productId}")
    public ApiResponse<CartResponse> updateItem(
            Authentication authentication,
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {

        User user = (User) authentication.getPrincipal();

        CartResponse response =
                cartService.updateItem(
                        user.getId(),
                        productId,
                        request
                );

        return ApiResponse.success(
                200,
                "Cart item updated successfully",
                response
        );
    }

    @DeleteMapping("/items/{productId}")
    public ApiResponse<Void> removeItem(
            Authentication authentication,
            @PathVariable UUID productId
    ) {

        User user = (User) authentication.getPrincipal();

        cartService.removeItem(
                user.getId(),
                productId
        );

        return ApiResponse.success(
                200,
                "Cart item removed successfully",
                null
        );
    }
}