package com.shiraku.orders.service;

import com.shiraku.orders.dto.OrderDTO;
import com.shiraku.orders.entity.OrderEntity;
import com.shiraku.orders.entity.Status;
import com.shiraku.orders.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void save(OrderEntity order) {
        orderRepository.save(order);
    }
    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }
}
