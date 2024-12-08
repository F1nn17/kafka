package com.shiraku.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiraku.payment.dto.OrderDTO;
import com.shiraku.payment.dto.SendOrderDTO;
import com.shiraku.payment.entity.PaymentEntity;
import com.shiraku.payment.entity.Status;
import com.shiraku.payment.service.PaymentService;
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
    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    public PaymentProcessor(KafkaTemplate<String, String> kafkaTemplate,
                            PaymentService paymentService, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.paymentService = paymentService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "new_orders", groupId = "payments-group")
    public void processPayment(String order, Acknowledgment ack) {
        processPaymentAsync(order, ack);
    }

    @Async
    public void processPaymentAsync(String messageOrder, Acknowledgment ack) {
        String orderMessage = "";
        try {
            OrderDTO order = objectMapper.readValue(messageOrder, OrderDTO.class);
            log.info("Received new order: {}", order);
            System.out.println("Processing payment for: " + order);
            //Thread.sleep(2500);
            PaymentEntity payment = new PaymentEntity();
            payment.setOrderId(order.getOrderId());
            payment.setAmount(order.getTotalPrice());
            payment.setStatus(Status.PAID_ORDER);
            //Thread.sleep(2500);
            log.info("Payment {} processed successfully for order: {}", payment, order);
            SendOrderDTO sendOrderDTO = getSendOrderDTO(order, payment);
            orderMessage = objectMapper.writeValueAsString(sendOrderDTO);
            kafkaTemplate.send("payed_orders", orderMessage);
            log.info("Message sent to payed_orders: {}", sendOrderDTO);
            //paymentService.save(payment);
            ack.acknowledge();
        } catch (Exception ex) {
            log.error("Error processing payment: {}", messageOrder, ex);
            kafkaTemplate.send("dead_letter_payments", orderMessage);
        }
    }

    private static SendOrderDTO getSendOrderDTO(OrderDTO order, PaymentEntity payment) {
        return new SendOrderDTO(
                order.getOrderId(),
                payment.getOrderId(),
                order.getUserId(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getCustomerAddress(),
                order.getProducts(),
                payment.getStatus()
                );
    }
}
