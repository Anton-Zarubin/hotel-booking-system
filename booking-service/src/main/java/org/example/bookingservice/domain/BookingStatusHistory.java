package org.example.bookingservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "booking_status_history")
public class BookingStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(name = "service_name")
    @Enumerated(EnumType.STRING)
    private ServiceName serviceName;

    @Column(name = "comment")
    private String comment;

    @CreationTimestamp
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    public BookingStatusHistory(Long id, BookingStatus status, ServiceName serviceName, String comment, Booking booking) {
        this.id = id;
        this.status = status;
        this.serviceName = serviceName;
        this.comment = comment;
        this.booking = booking;
    }
}
