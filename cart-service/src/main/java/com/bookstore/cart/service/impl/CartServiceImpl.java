package com.bookstore.cart.service.impl;

import com.bookstore.cart.dto.request.AddToCartRequest;
import com.bookstore.cart.dto.request.UpdateCartRequest;
import com.bookstore.cart.dto.response.CartResponse;
import com.bookstore.cart.feign.ProductServiceClient;
import com.bookstore.cart.model.Cart;
import com.bookstore.cart.model.CartItem;
import com.bookstore.cart.service.CartService;
import com.bookstore.common.exception.ResourceNotFoundException;
import com.bookstore.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductServiceClient productServiceClient;

    private static final String CART_KEY = "cart:";

    private Cart getCartFromRedis(String userId) {
        Cart cart = (Cart) redisTemplate.opsForValue().get(CART_KEY + userId);
        if (cart == null) {
            cart = Cart.builder()
                    .userId(userId)
                    .items(new ArrayList<>())
                    .totalAmount(BigDecimal.ZERO)
                    .build();
        }
        return cart;
    }

    private void saveCart(Cart cart) {
        redisTemplate.opsForValue().set(CART_KEY + cart.getUserId(), cart);
    }

    private CartResponse toResponse(Cart cart) {
        return CartResponse.builder()
                .userId(cart.getUserId())
                .items(cart.getItems())
                .totalAmount(cart.getTotalAmount())
                .totalItems(cart.getItems().size())
                .build();
    }

    private BigDecimal calculateTotal(Cart cart) {
        return cart.getItems().stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public ApiResponse<CartResponse> getCart(String userId) {
        Cart cart = getCartFromRedis(userId);
        return ApiResponse.success(toResponse(cart));
    }

    @Override
    public ApiResponse<CartResponse> addToCart(String userId, AddToCartRequest request) {
        ApiResponse<Map<String, Object>> productResponse =
                productServiceClient.getProductById(request.getProductId());

        if (productResponse == null || productResponse.getData() == null) {
            throw new ResourceNotFoundException("Product not found");
        }

        Map<String, Object> product = productResponse.getData();
        String title = (String) product.get("title");
        String author = (String) product.get("author");
        BigDecimal price = new BigDecimal(product.get("price").toString());

        Cart cart = getCartFromRedis(userId);

        boolean exists = false;
        for (CartItem item : cart.getItems()) {
            if (item.getProductId().equals(request.getProductId())) {
                item.setQuantity(item.getQuantity() + request.getQuantity());
                item.setSubtotal(price.multiply(BigDecimal.valueOf(item.getQuantity())));
                exists = true;
                break;
            }
        }

        if (!exists) {
            CartItem newItem = CartItem.builder()
                    .productId(request.getProductId())
                    .productTitle(title)
                    .author(author)
                    .quantity(request.getQuantity())
                    .unitPrice(price)
                    .subtotal(price.multiply(BigDecimal.valueOf(request.getQuantity())))
                    .build();
            cart.getItems().add(newItem);
        }

        cart.setTotalAmount(calculateTotal(cart));
        saveCart(cart);

        return ApiResponse.success(toResponse(cart));
    }

    @Override
    public ApiResponse<CartResponse> updateCart(String userId, UpdateCartRequest request) {
        Cart cart = getCartFromRedis(userId);

        for (CartItem item : cart.getItems()) {
            if (item.getProductId().equals(request.getProductId())) {
                item.setQuantity(request.getQuantity());
                item.setSubtotal(item.getUnitPrice()
                        .multiply(BigDecimal.valueOf(request.getQuantity())));
                break;
            }
        }

        cart.setTotalAmount(calculateTotal(cart));
        saveCart(cart);

        return ApiResponse.success(toResponse(cart));
    }

    @Override
    public ApiResponse<CartResponse> removeFromCart(String userId, Long productId) {
        Cart cart = getCartFromRedis(userId);
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        cart.setTotalAmount(calculateTotal(cart));
        saveCart(cart);
        return ApiResponse.success(toResponse(cart));
    }

    @Override
    public ApiResponse<Void> clearCart(String userId) {
        redisTemplate.delete(CART_KEY + userId);
        return ApiResponse.success(null);
    }

    @Override
    public ApiResponse<CartResponse> getCartTotal(String userId) {
        Cart cart = getCartFromRedis(userId);
        return ApiResponse.success(toResponse(cart));
    }
}