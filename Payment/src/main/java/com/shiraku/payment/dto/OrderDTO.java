package com.shiraku.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private UUID orderId;
    private Long userId;
    private String customerName;
    private String customerEmail;
    private String customerAddress;
    private Set<ProductDTO> products;
    private String status;

    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (ProductDTO product : products) {
            totalPrice = totalPrice.add(product.getPrice());
        }
        return totalPrice;
    }
}
