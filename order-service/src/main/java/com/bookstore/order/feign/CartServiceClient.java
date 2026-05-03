package com.bookstore.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "CART-SERVICE")
public interface CartServiceClient {

    @GetMapping("/api/cart")
    CartResponse getCart(@RequestHeader("Authorization") String token);

    @DeleteMapping("/api/cart/clear")
    void clearCart(@RequestHeader("Authorization") String token);
}