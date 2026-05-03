package com.bookstore.admin.figen;

import com.bookstore.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductServiceClient {

    @PutMapping("/api/products/{id}")
    ApiResponse<Object> updateProduct(@PathVariable Long id,
                                      @RequestBody Map<String, Object> request,
                                      @RequestHeader("Authorization") String token);

    @DeleteMapping("/api/products/{id}")
    ApiResponse<Object> deleteProduct(@PathVariable Long id,
                                      @RequestHeader("Authorization") String token);
}