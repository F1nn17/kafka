package com.shiraku.orders.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiraku.orders.dto.OrderDTO;
import com.shiraku.orders.dto.SendOrderDTO;
import com.shiraku.orders.entity.OrderEntity;
import com.shiraku.orders.entity.Status;
import com.shiraku.orders.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    public OrderController(KafkaTemplate<String, String> kafkaTemplate,
                           ObjectMapper objectMapper,
                           OrderService orderService) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.orderService = orderService;
    }

    @GetMapping
    public  ResponseEntity<List<OrderEntity>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PostMapping("/new_order")
    public ResponseEntity<String> createOrder(@RequestBody OrderDTO order) {
        try {
            log.info("An order has been received");
            OrderEntity orderEnt = new OrderEntity();
            orderEnt.setUserId(order.getUserId());
            orderEnt.setCustomerName(order.getCustomerName());
            orderEnt.setStatus(Status.CREATED);
            SendOrderDTO sendOrder = new SendOrderDTO(
                    orderEnt.getId(),
                    order.getUserId(),
                    order.getCustomerName(),
                    order.getCustomerEmail(),
                    order.getCustomerAddress(),
                    order.getProducts(),
                    orderEnt.getStatus()
            );
            //orderService.save(orderEnt);
            String messageOrder =  objectMapper.writeValueAsString(sendOrder);
            kafkaTemplate.send("new_orders", messageOrder);
            log.info("Order created successfully!");
            log.info("Order {}", order);
            return ResponseEntity.ok("Order created: " + order);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
