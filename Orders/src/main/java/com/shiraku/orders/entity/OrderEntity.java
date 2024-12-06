package com.shiraku.orders.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
public class OrderEntity {
    @Id
    private UUID id;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "status", nullable = false)
    private Status status;

    public OrderEntity() {
        id = UUID.randomUUID();
    }
}
