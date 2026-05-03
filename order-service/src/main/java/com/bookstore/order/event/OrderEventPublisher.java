package com.bookstore.order.event;

import com.bookstore.order.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void publishOrderPlaced(Order order) {
        OrderEvent event = OrderEvent.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .type("ORDER_PLACED")
                .timestamp(LocalDateTime.now())
                .build();
        kafkaTemplate.send("order-events", event);
        log.info("Published ORDER_PLACED event for order: {}", order.getId());
    }

    public void publishOrderStatusChanged(Order order) {
        OrderEvent event = OrderEvent.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .type("ORDER_" + order.getStatus().name())
                .timestamp(LocalDateTime.now())
                .build();
        kafkaTemplate.send("order-events", event);
        log.info("Published ORDER_{} event for order: {}", order.getStatus(), order.getId());
    }
}