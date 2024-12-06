package com.shiraku.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long userId;
    private String customerName;
    private String customerEmail;
    private String customerAddress;
    private Set<ProductDTO> products;
}

