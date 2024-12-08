package com.shiraku.payment.service;

import com.shiraku.payment.dto.OrderDTO;
import com.shiraku.payment.entity.PaymentEntity;
import com.shiraku.payment.entity.Status;
import com.shiraku.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public void save(PaymentEntity payment) {
        paymentRepository.save(payment);
    }
}
