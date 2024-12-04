package com.shiraku.notifications;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationSender {
    @KafkaListener(topics = "sent_orders", groupId = "notifications-group")
    public void sendNotification(String message) {
        System.out.println("Sending notification: " + message);
        // Here, you can integrate email, SMS, or push notifications logic.
    }
}
