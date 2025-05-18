package org.example.notificationservice.service;

import org.example.notificationservice.dto.BookingKafkaDto;

public interface KafkaService {

    void produce(BookingKafkaDto bookingKafkaDto);
}
