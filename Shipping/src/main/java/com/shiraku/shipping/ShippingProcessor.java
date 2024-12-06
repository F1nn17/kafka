package com.shiraku.shipping;

import com.shiraku.shipping.dto.OrderDTO;
import com.shiraku.shipping.dto.SendOrderDTO;
import com.shiraku.shipping.entity.ShipmentEntity;
import com.shiraku.shipping.service.ShipmentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class ShippingProcessor {
    private final KafkaTemplate<String, SendOrderDTO> kafkaTemplate;
    private final ModelMapper modelMapper;
    private final ShipmentService shipmentService;

    public ShippingProcessor(KafkaTemplate<String,  SendOrderDTO> kafkaTemplate, ModelMapper modelMapper, ShipmentService shipmentService) {
        this.kafkaTemplate = kafkaTemplate;
        this.modelMapper = modelMapper;
        this.shipmentService = shipmentService;
    }

    @KafkaListener(topics = "payed_orders", groupId = "shipping-group")
    public void shipOrder(OrderDTO order) {
        processOrderAsync(order);
    }

    @Async
    public void processOrderAsync(OrderDTO orderDTO) {
        try {
            log.info("Processing shipping for order: {}", orderDTO);
            Thread.sleep(7000);
            System.out.println("Shipping order: " + orderDTO);
            ShipmentEntity shipment = new ShipmentEntity();
            shipment.setOrderId(orderDTO.getOrderId());
            shipment.setPaymentId(orderDTO.getPaymentId());
            shipment.setShippingAddress(orderDTO.getCustomerAddress());
            shipment.setStatus(orderDTO.getStatus());
            Thread.sleep(3000);
            SendOrderDTO send = modelMapper.map(orderDTO, SendOrderDTO.class);
            kafkaTemplate.send("sent_orders", send);
            shipmentService.save(shipment);
            log.debug("Shipping details: {}", shipment);
        } catch (Exception ex) {
            log.error("Error processing shipping for order: {}", orderDTO, ex);
        }
        System.out.println("Processing asynchronously: " + orderDTO);
    }

}
