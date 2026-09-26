package com.example.ecommerce.product;

import com.example.ecommerce.category.Category;
import com.example.ecommerce.category.CategoryRepository;
import com.example.ecommerce.common.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ProductResponse create(CreateProductRequest request) {

        Category category = categoryRepository
                .findById(request.categoryId())
                .orElseThrow(() ->
                        new AppException(
                                404,
                                "Category not found"
                        )
                );

        if (category.getDeletedAt() != null) {
            throw new AppException(
                    404,
                    "Category not found"
            );
        }

        Product product = new Product();

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStock(request.stock());
        product.setCategory(category);
        product.setStatus(ProductStatus.ACTIVE);

        Product saved = productRepository.save(product);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {

        return productRepository
                .findAllByDeletedAtIsNull()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ProductResponse toResponse(Product product) {

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getStatus()
        );
    }
}