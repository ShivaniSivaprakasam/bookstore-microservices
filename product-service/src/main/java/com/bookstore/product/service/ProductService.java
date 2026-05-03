package com.bookstore.product.service;

import com.bookstore.common.response.ApiResponse;
import com.bookstore.product.dto.request.CategoryRequest;
import com.bookstore.product.dto.request.ProductRequest;
import com.bookstore.product.dto.response.CategoryResponse;
import com.bookstore.product.dto.response.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    // Product
    ApiResponse<Page<ProductResponse>> getAllProducts(int page, int size);
    ApiResponse<ProductResponse> getProductById(Long id);
    ApiResponse<List<ProductResponse>> searchProducts(String query);
    ApiResponse<List<ProductResponse>> getProductsByCategory(Long categoryId);
    ApiResponse<ProductResponse> createProduct(ProductRequest request);
    ApiResponse<ProductResponse> updateProduct(Long id, ProductRequest request);
    ApiResponse<Void> deleteProduct(Long id);

    // Category
    ApiResponse<List<CategoryResponse>> getAllCategories();
    ApiResponse<CategoryResponse> createCategory(CategoryRequest request);
}