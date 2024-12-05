package com.shiraku.orders.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class OrderListener {
    @KafkaListener(topics = "new_orders", groupId = "orders-group")
    public void listen(String message) {
        try {
            log.info("Sending notification for order: {}", message);
            System.out.println("New order received: " + message);
            log.debug("Notification content: {}", message);
        } catch (Exception ex) {
            log.error("Error sending notification for order: {}", message, ex);
        }
    }
}
