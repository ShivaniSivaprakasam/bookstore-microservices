package com.bookstore.notification.consumer;

import com.bookstore.notification.event.OrderEvent;
import com.bookstore.notification.event.UserEvent;
import com.bookstore.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "order-events", groupId = "notification-group")
    public void onOrderEvent(OrderEvent event) {
        log.info("Received order event: {} for orderId: {}", event.getType(), event.getOrderId());

        switch (event.getType()) {
            case "ORDER_PLACED" -> emailService.sendOrderConfirmation(event);
            case "ORDER_SHIPPED" -> emailService.sendShippingUpdate(event);
            case "ORDER_DELIVERED" -> emailService.sendDeliveryConfirmation(event);
            case "ORDER_CANCELLED" -> emailService.sendOrderCancellation(event);
            default -> log.warn("Unknown order event type: {}", event.getType());
        }
    }

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void onUserEvent(UserEvent event) {
        log.info("Received user event: {} for userId: {}", event.getType(), event.getUserId());

        if ("USER_REGISTERED".equals(event.getType())) {
            emailService.sendWelcomeEmail(event);
        }
    }
}