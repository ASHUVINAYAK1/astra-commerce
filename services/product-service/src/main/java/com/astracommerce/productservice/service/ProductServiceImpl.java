package com.astracommerce.productservice.service;

import com.astracommerce.productservice.dto.ProductRequest;
import com.astracommerce.productservice.dto.ProductResponse;
import com.astracommerce.productservice.entity.Product;
import com.astracommerce.productservice.entity.ProductStatus;
import com.astracommerce.productservice.exception.ResourceNotFoundException;
import com.astracommerce.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating product: {}", request.getName());
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .status(request.getStock() > 0 ? ProductStatus.ACTIVE : ProductStatus.OUT_OF_STOCK)
                .build();
        return toResponse(productRepository.save(product));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        log.info("Updating product id: {}", id);
        Product product = findOrThrow(id);
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setStatus(request.getStock() > 0 ? ProductStatus.ACTIVE : ProductStatus.OUT_OF_STOCK);
        return toResponse(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product id: {}", id);
        productRepository.delete(findOrThrow(id));
    }

    @Override
    public ProductResponse updateStock(Long id, int quantity) {
        Product product = findOrThrow(id);
        int newStock = product.getStock() + quantity;
        if (newStock < 0) throw new IllegalStateException("Insufficient stock for product id: " + id);
        product.setStock(newStock);
        product.setStatus(newStock > 0 ? ProductStatus.ACTIVE : ProductStatus.OUT_OF_STOCK);
        return toResponse(productRepository.save(product));
    }

    private Product findOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    private ProductResponse toResponse(Product p) {
        return ProductResponse.builder()
                .id(p.getId()).name(p.getName()).description(p.getDescription())
                .price(p.getPrice()).stock(p.getStock()).status(p.getStatus())
                .createdAt(p.getCreatedAt()).build();
    }
}
