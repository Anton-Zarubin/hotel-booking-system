package org.example.hotelservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "booking")
public class Booking {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "check-in", nullable = false)
    private LocalDate checkIn;

    @Column(name = "check-out", nullable = false)
    private LocalDate checkOut;
}
