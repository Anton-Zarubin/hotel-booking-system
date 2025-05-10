package org.example.bookingservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "check-in", nullable = false)
    private LocalDate checkIn;

    @Column(name = "check-out", nullable = false)
    private LocalDate checkOut;

    @CreationTimestamp
    @Column(name = "creation_time")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_time")
    private LocalDateTime modifiedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @OneToMany(
            mappedBy = "booking",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<BookingStatusHistory> bookingStatusHistory = new ArrayList<>();

    public void addStatusHistory(BookingStatus status, ServiceName serviceName, String comment) {
        getBookingStatusHistory().add(new BookingStatusHistory(null, status, serviceName, comment, this));
    }

    public Booking(Long userId, Long roomId, LocalDate checkIn, LocalDate checkOut, BookingStatus status) {
        this.userId = userId;
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
    }
}
