package com.shiraku.shipping;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ShippingProcessor {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public ShippingProcessor(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "payed_orders", groupId = "shipping-group")
    public void shipOrder(String message) {
        System.out.println("Shipping order: " + message);
        kafkaTemplate.send("sent_orders", "Order shipped: " + message);
    }
}
