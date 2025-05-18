package org.example.paymentservice.dto;

public record ErrorKafkaDto(Long bookingId, StatusDto statusDto) {
}
