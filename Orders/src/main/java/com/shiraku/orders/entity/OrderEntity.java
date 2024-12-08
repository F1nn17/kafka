package com.shiraku.orders.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    public OrderEntity() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
