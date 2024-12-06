package com.shiraku.shipping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendOrderDTO {
    private UUID orderId;
    private String customerAddress;
}
