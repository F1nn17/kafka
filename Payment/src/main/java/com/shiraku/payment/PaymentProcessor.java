package com.shiraku.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class PaymentProcessor {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public PaymentProcessor(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "new_orders", groupId = "payments-group")
    public void processPayment(String order, Acknowledgment ack) {
        processPaymentAsync(order, ack);
    }

    @Async
    public void processPaymentAsync(String order, Acknowledgment ack) {
        try {
            log.info("Received new order: {}", order);
            System.out.println("Processing payment for: " + order);
            Thread.sleep(1500);
            log.info("Payment processed successfully for order: {}", order);
            kafkaTemplate.send("payed_orders", "Payment successful for: " + order);
            log.info("Message sent to payed_orders: {}", order);

            ack.acknowledge();
        } catch (Exception ex) {
            log.error("Error processing payment: {}", order, ex);
            kafkaTemplate.send("dead_letter_payments", order);
        }
    }
}
