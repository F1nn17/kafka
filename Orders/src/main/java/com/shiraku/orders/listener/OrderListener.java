package com.shiraku.orders.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {
    @KafkaListener(topics = "new_orders", groupId = "orders-group")
    public void listen(String message) {
        System.out.println("New order received: " + message);
    }
}
