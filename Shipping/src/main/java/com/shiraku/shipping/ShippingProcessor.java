package com.shiraku.shipping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiraku.shipping.dto.OrderDTO;
import com.shiraku.shipping.dto.SendOrderDTO;
import com.shiraku.shipping.dto.Status;
import com.shiraku.shipping.entity.ShipmentEntity;
import com.shiraku.shipping.service.ShipmentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Slf4j
@Component
public class ShippingProcessor {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ModelMapper modelMapper;
    private final ShipmentService shipmentService;
    private final ObjectMapper objectMapper;

    public ShippingProcessor(KafkaTemplate<String,  String> kafkaTemplate,
                             ModelMapper modelMapper,
                             ShipmentService shipmentService,
                             ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.modelMapper = modelMapper;
        this.shipmentService = shipmentService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "payed_orders", groupId = "shipping-group")
    public void shipOrder(String order) {
        processOrderAsync(order);
    }

    @Async
    public void processOrderAsync(String messageOrder) {
        try {
            OrderDTO orderDTO = objectMapper.readValue(messageOrder, OrderDTO.class);
            log.info("Processing shipping for order: {}", orderDTO);
            //Thread.sleep(7000);
            System.out.println("Shipping order: " + orderDTO);
            ShipmentEntity shipment = new ShipmentEntity(
                    UUID.randomUUID(),
                    orderDTO.getOrderId(),
                    orderDTO.getPaymentId(),
                    orderDTO.getCustomerAddress(),
                    Status.SHIPPED
            );
           // Thread.sleep(3000);
            SendOrderDTO send = new SendOrderDTO(
                    orderDTO.getOrderId(),
                    orderDTO.getCustomerAddress()
            );
            String sendOrder = objectMapper.writeValueAsString(send);
            kafkaTemplate.send("sent_orders", sendOrder);
            //shipmentService.save(shipment);
            log.debug("Shipping details: {}", shipment);
        } catch (Exception ex) {
            log.error("Error processing shipping for order: {}", messageOrder, ex);
        }
        log.info("Processing asynchronously: {}", messageOrder);
    }

}
