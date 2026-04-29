package com.astracommerce.productservice.service;

import com.astracommerce.productservice.dto.ProductRequest;
import com.astracommerce.productservice.dto.ProductResponse;
import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse getProductById(Long id);
    List<ProductResponse> getAllProducts();
    List<ProductResponse> searchProducts(String name);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
    /** Called by Inventory service or internal logic to adjust stock */
    ProductResponse updateStock(Long id, int quantity);
}
