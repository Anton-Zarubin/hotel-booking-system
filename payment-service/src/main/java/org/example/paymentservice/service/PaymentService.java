package org.example.paymentservice.service;

import org.example.paymentservice.dto.PaymentKafkaDto;

public interface PaymentService {

    void pay(PaymentKafkaDto paymentKafkaDto);
}
