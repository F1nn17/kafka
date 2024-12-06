package com.shiraku.orders.controllers;

import com.shiraku.orders.dto.OrderDTO;
import com.shiraku.orders.dto.SendOrderDTO;
import com.shiraku.orders.entity.OrderEntity;
import com.shiraku.orders.entity.Status;
import com.shiraku.orders.service.OrderService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final KafkaTemplate<String, SendOrderDTO> kafkaTemplate;
    private final ModelMapper modelMapper;
    private final OrderService orderService;

    public OrderController(KafkaTemplate<String, SendOrderDTO> kafkaTemplate, ModelMapper modelMapper, OrderService orderService) {
        this.kafkaTemplate = kafkaTemplate;
        this.modelMapper = modelMapper;
        this.orderService = orderService;
    }

    @PostMapping("/new_order")
    public ResponseEntity<String> createOrder(@RequestBody OrderDTO order) {
        SendOrderDTO sendOrder = modelMapper.map(order, SendOrderDTO.class);
        OrderEntity orderEnt = modelMapper.map(order, OrderEntity.class);
        orderEnt.setStatus(Status.CREATED);
        sendOrder.setOrderId(orderEnt.getId());
        sendOrder.setStatus(orderEnt.getStatus());
        kafkaTemplate.send("new_orders", sendOrder);
        orderService.saveOrder(orderEnt);
        return ResponseEntity.ok("Order created: " + order);
    }
}
