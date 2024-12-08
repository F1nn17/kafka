package com.shiraku.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Data
@Slf4j
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
        if (products == null) {
            return totalPrice;
        }
        for (ProductDTO product : products) {
            if (product == null) {
                log.warn("Encountered null product in the list.");
                continue;
            }
            if (product.getPrice() == null) {
                log.warn("Product '{}' has null price.", product.getName());
                continue;
            }
            totalPrice = totalPrice.add(product.getPrice());
        }

        return totalPrice;
    }
}
