package com.shiraku.payment.dto;

import com.shiraku.payment.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendOrderDTO {
    private UUID orderId;
    private UUID paymentId;
    private Long userId;
    private String customerName;
    private String customerEmail;
    private String customerAddress;
    private Set<ProductDTO> products;
    private Status status;
}
