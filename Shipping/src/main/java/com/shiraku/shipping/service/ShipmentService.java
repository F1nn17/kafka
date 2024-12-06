package com.shiraku.shipping.service;

import com.shiraku.shipping.entity.ShipmentEntity;
import com.shiraku.shipping.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

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
