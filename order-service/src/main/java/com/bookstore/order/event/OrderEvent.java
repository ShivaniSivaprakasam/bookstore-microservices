package com.bookstore.order.event;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEvent {

    private Long orderId;
    private Long userId;
    private String type;
    private LocalDateTime timestamp;
}