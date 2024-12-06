package com.shiraku.orders.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class OrderListener {
    @KafkaListener(topics = "sent_orders", groupId = "notifications-group")
    public void listen(Map<String, Object> message) {
        try {
            System.out.println("Мне лень думать уже");
        } catch (Exception ex) {
            log.error("Error sending notification for order: {}", message, ex);
        }
    }
}
