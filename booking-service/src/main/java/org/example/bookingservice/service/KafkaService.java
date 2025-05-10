package org.example.bookingservice.service;

import org.example.bookingservice.dto.HotelKafkaDto;

public interface KafkaService {

    void produce(HotelKafkaDto hotelKafkaDto);
}
