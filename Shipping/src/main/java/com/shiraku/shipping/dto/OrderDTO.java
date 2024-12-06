package com.shiraku.shipping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private UUID orderId;
    private UUID paymentId;
    private Long userId;
    private String customerName;
    private String customerEmail;
    private String customerAddress;
    private Set<ProductDTO> products;
    private Status status;
}
