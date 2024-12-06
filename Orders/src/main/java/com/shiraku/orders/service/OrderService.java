package com.shiraku.orders.service;

import com.shiraku.orders.entity.OrderEntity;
import com.shiraku.orders.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void saveOrder(OrderEntity order) {
        orderRepository.save(order);
    }
}
