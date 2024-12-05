package com.shiraku.notifications;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class NotificationSender {
    @KafkaListener(topics = "sent_orders", groupId = "notifications-group")
    public void sendNotification(String message) {
        sendNotificationAsync(message);
    }

    @Async
    public void sendNotificationAsync(String message) {
        try {
            log.info("Sending notification for order: {}", message);
            System.out.println("Sending notification: " + message);
            log.debug("Notification content: {}", message);
        } catch (Exception ex) {
            log.error("Error sending notification for order: {}", message, ex);
        }
    }
}
