package com.bookstore.product.service.impl;

import com.bookstore.common.exception.ResourceNotFoundException;
import com.bookstore.common.response.ApiResponse;
import com.bookstore.product.dto.request.CategoryRequest;
import com.bookstore.product.dto.request.ProductRequest;
import com.bookstore.product.dto.response.CategoryResponse;
import com.bookstore.product.dto.response.ProductResponse;
import com.bookstore.product.entity.Category;
import com.bookstore.product.entity.Product;
import com.bookstore.product.mapper.CategoryMapper;
import com.bookstore.product.mapper.ProductMapper;
import com.bookstore.product.repository.CategoryRepository;
import com.bookstore.product.repository.ProductRepository;
import com.bookstore.product.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;

    @Override
    public ApiResponse<Page<ProductResponse>> getAllProducts(int page, int size) {
        Page<ProductResponse> products = productRepository
                .findAll(PageRequest.of(page, size))
                .map(productMapper::toResponse);
        return ApiResponse.success(products);
    }

    @Override
    public ApiResponse<ProductResponse> getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return ApiResponse.success(productMapper.toResponse(product));
    }

    @Override
    public ApiResponse<List<ProductResponse>> searchProducts(String query) {
        List<ProductResponse> products = productRepository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query)
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.success(products);
    }

    @Override
    public ApiResponse<List<ProductResponse>> getProductsByCategory(Long categoryId) {
        List<ProductResponse> products = productRepository
                .findByCategoryId(categoryId)
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.success(products);
    }

    @Override
    public ApiResponse<ProductResponse> createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Product product = productMapper.toEntity(request, category);
        Product saved = productRepository.save(product);
        return ApiResponse.created(productMapper.toResponse(saved));
    }

    @Override
    public ApiResponse<ProductResponse> updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        product.setTitle(request.getTitle());
        product.setAuthor(request.getAuthor());
        product.setIsbn(request.getIsbn());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        Product updated = productRepository.save(product);
        return ApiResponse.success(productMapper.toResponse(updated));
    }

    @Override
    public ApiResponse<Void> deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        productRepository.delete(product);
        return ApiResponse.success(null);
    }

    @Override
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.success(categories);
    }

    @Override
    public ApiResponse<CategoryResponse> createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            return ApiResponse.error("Category already exists", 400);
        }
        Category category = categoryMapper.toEntity(request);
        Category saved = categoryRepository.save(category);
        return ApiResponse.created(categoryMapper.toResponse(saved));
    }
}