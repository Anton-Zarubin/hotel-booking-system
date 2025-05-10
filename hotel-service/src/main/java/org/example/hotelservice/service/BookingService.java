package org.example.hotelservice.service;

import org.example.hotelservice.dto.ErrorKafkaDto;
import org.example.hotelservice.dto.HotelKafkaDto;

public interface BookingService {

    void book(HotelKafkaDto hotelKafkaDto);

    void cancelBooking(ErrorKafkaDto errorKafkaDto);
}
