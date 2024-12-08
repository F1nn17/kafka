package com.shiraku.shipping.service;

import com.shiraku.shipping.dto.OrderDTO;
import com.shiraku.shipping.dto.Status;
import com.shiraku.shipping.entity.ShipmentEntity;
import com.shiraku.shipping.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ShipmentService {
    private final ShipmentRepository shipmentRepository;

    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public void save(ShipmentEntity entity) {
        shipmentRepository.save(entity);
    }
}
