package org.example.notificationservice.service;

import org.example.notificationservice.dto.NotificationKafkaDto;

public interface NotificationService {

    void send(NotificationKafkaDto notificationKafkaDto);
}
