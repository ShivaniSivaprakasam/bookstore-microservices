package com.bookstore.notification.service;

import com.bookstore.notification.event.OrderEvent;
import com.bookstore.notification.event.UserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    public void sendOrderConfirmation(OrderEvent event) {
        log.info("Sending ORDER CONFIRMATION email for orderId: {} to userId: {}",
                event.getOrderId(), event.getUserId());
    }

    public void sendShippingUpdate(OrderEvent event) {
        log.info("Sending SHIPPING UPDATE email for orderId: {} to userId: {}",
                event.getOrderId(), event.getUserId());
    }

    public void sendDeliveryConfirmation(OrderEvent event) {
        log.info("Sending DELIVERY CONFIRMATION email for orderId: {} to userId: {}",
                event.getOrderId(), event.getUserId());
    }

    public void sendOrderCancellation(OrderEvent event) {
        log.info("Sending ORDER CANCELLATION email for orderId: {} to userId: {}",
                event.getOrderId(), event.getUserId());
    }

    public void sendWelcomeEmail(UserEvent event) {
        log.info("Sending WELCOME email to userId: {} email: {}",
                event.getUserId(), event.getEmail());
    }
}