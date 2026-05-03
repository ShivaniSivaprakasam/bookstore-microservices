package com.bookstore.order.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusRequest {

    @NotNull(message = "Status is required")
    private String status;
}