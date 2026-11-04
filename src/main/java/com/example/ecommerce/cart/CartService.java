package com.example.ecommerce.cart;

import com.example.ecommerce.common.AppException;
import com.example.ecommerce.product.Product;
import com.example.ecommerce.product.ProductRepository;
import com.example.ecommerce.user.User;
import com.example.ecommerce.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public CartResponse addItem(
            UUID userId,
            AddCartItemRequest request
    ) {

        User user = userRepository
                .findById(requestUserId(userId))
                .orElseThrow(() ->
                        new AppException(404, "User not found")
                );

        Product product = productRepository
                .findById(request.productId())
                .orElseThrow(() ->
                        new AppException(404, "Product not found")
                );

        if (product.getDeletedAt() != null) {
            throw new AppException(404, "Product not found");
        }

        if (product.getStatus() !=
                com.example.ecommerce.product.ProductStatus.ACTIVE) {
            throw new AppException(
                    400,
                    "Product is inactive"
            );
        }

        if (request.quantity() > product.getStock()) {
            throw new AppException(
                    400,
                    "Insufficient product stock"
            );
        }

        Cart cart = cartRepository
                .findByUserId(userId)
                .orElseGet(() -> {

                    Cart newCart = new Cart();
                    newCart.setUser(user);

                    return cartRepository.save(newCart);
                });

        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductId(
                        cart.getId(),
                        product.getId()
                )
                .orElse(null);

        if (cartItem == null) {

            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.quantity());

        } else {

            int newQuantity =
                    cartItem.getQuantity() + request.quantity();

            if (newQuantity > product.getStock()) {
                throw new AppException(
                        400,
                        "Insufficient product stock"
                );
            }

            cartItem.setQuantity(newQuantity);
        }

        cartItemRepository.save(cartItem);

        return getCart(userId);
    }

    @Transactional(readOnly = true)
    public CartResponse getCart(UUID userId) {

        Cart cart = cartRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new AppException(
                                404,
                                "Cart not found"
                        )
                );

        return toResponse(cart);
    }

    @Transactional
    public CartResponse updateItem(
            UUID userId,
            UUID productId,
            UpdateCartItemRequest request
    ) {

        Cart cart = cartRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new AppException(
                                404,
                                "Cart not found"
                        )
                );

        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductId(
                        cart.getId(),
                        productId
                )
                .orElseThrow(() ->
                        new AppException(
                                404,
                                "Cart item not found"
                        )
                );

        Product product = cartItem.getProduct();

        if (request.quantity() > product.getStock()) {
            throw new AppException(
                    400,
                    "Insufficient product stock"
            );
        }

        cartItem.setQuantity(request.quantity());

        cartItemRepository.save(cartItem);

        return toResponse(cart);
    }

    @Transactional
    public void removeItem(
            UUID userId,
            UUID productId
    ) {

        Cart cart = cartRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new AppException(
                                404,
                                "Cart not found"
                        )
                );

        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductId(
                        cart.getId(),
                        productId
                )
                .orElseThrow(() ->
                        new AppException(
                                404,
                                "Cart item not found"
                        )
                );

        cartItemRepository.delete(cartItem);
    }

    private CartResponse toResponse(Cart cart) {

        List<CartResponse.CartItemResponse> items =
                cartItemRepository
                        .findAllByCartId(cart.getId())
                        .stream()
                        .map(item -> {

                            Product product = item.getProduct();

                            BigDecimal subtotal =
                                    product.getPrice()
                                            .multiply(
                                                    BigDecimal.valueOf(
                                                            item.getQuantity()
                                                    )
                                            );

                            return new CartResponse.CartItemResponse(
                                    product.getId(),
                                    product.getName(),
                                    product.getPrice(),
                                    item.getQuantity(),
                                    subtotal
                            );
                        })
                        .toList();

        BigDecimal total = items.stream()
                .map(CartResponse.CartItemResponse::subtotal)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

        return new CartResponse(
                cart.getId(),
                items,
                total
        );
    }

    private UUID requestUserId(UUID userId) {
        return userId;
    }
}