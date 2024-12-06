package com.shiraku.payment;

import com.shiraku.payment.dto.OrderDTO;
import com.shiraku.payment.dto.SendOrderDTO;
import com.shiraku.payment.entity.PaymentEntity;
import com.shiraku.payment.entity.Status;
import com.shiraku.payment.repository.PaymentRepository;
import com.shiraku.payment.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class PaymentProcessor {
    private final KafkaTemplate<String, SendOrderDTO> kafkaTemplate;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;

    public PaymentProcessor(KafkaTemplate<String, SendOrderDTO> kafkaTemplate, ModelMapper modelMapper, PaymentRepository paymentRepository, PaymentService paymentService) {
        this.kafkaTemplate = kafkaTemplate;
        this.modelMapper = modelMapper;
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = "new_orders", groupId = "payments-group")
    public void processPayment(OrderDTO order, Acknowledgment ack) {
        processPaymentAsync(order, ack);
    }

    @Async
    public void processPaymentAsync(OrderDTO order, Acknowledgment ack) {
        try {
            log.info("Received new order: {}", order);
            System.out.println("Processing payment for: " + order);
            Thread.sleep(2500);
            PaymentEntity payment = new PaymentEntity();
            payment.setOrderId(order.getOrderId());
            payment.setAmount(order.getTotalPrice());
            payment.setStatus(Status.PAID_ORDER);
            Thread.sleep(2500);
            log.info("Payment {} processed successfully for order: {}", payment ,order);
            SendOrderDTO sendOrderDTO = modelMapper.map(order, SendOrderDTO.class);
            sendOrderDTO.setPaymentId(payment.getId());
            sendOrderDTO.setStatus(payment.getStatus());
            kafkaTemplate.send("payed_orders", sendOrderDTO);
            log.info("Message sent to payed_orders: {}", sendOrderDTO);
            paymentService.save(payment);
            ack.acknowledge();
        } catch (Exception ex) {
            log.error("Error processing payment: {}", order, ex);
            SendOrderDTO sendOrderDTO = modelMapper.map(order, SendOrderDTO.class);
            kafkaTemplate.send("dead_letter_payments", sendOrderDTO);
        }
    }
}
