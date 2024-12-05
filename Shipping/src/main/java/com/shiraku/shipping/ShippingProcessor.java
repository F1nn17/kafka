package com.shiraku.shipping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class ShippingProcessor {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public ShippingProcessor(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "payed_orders", groupId = "shipping-group")
    public void shipOrder(String message) {
        processOrderAsync(message);
    }

    @Async
    public void processOrderAsync(String message) {
        try {
            log.info("Processing shipping for order: {}", message);
            System.out.println("Shipping order: " + message);
            kafkaTemplate.send("sent_orders", "Order shipped: " + message);
            log.debug("Shipping details: {}", message);
        } catch (Exception ex) {
            log.error("Error processing shipping for order: {}", message, ex);
        }
        System.out.println("Processing asynchronously: " + message);
    }

}
