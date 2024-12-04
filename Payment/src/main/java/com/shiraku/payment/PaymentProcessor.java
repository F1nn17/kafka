package com.shiraku.payment;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentProcessor {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public PaymentProcessor(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "new_orders", groupId = "payments-group")
    public void processPayment(String order) {
        System.out.println("Processing payment for: " + order);
        kafkaTemplate.send("payed_orders", "Payment successful for: " + order);
    }
}
