package org.example.bookingservice.dto;

public record ErrorKafkaDto(Long bookingId, StatusDto statusDto) {
}
