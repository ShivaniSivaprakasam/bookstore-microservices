package com.bookstore.product.repository;

import com.bookstore.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
            String title, String author);
}