package org.example.hotelservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column
    private int number;

    @Column(nullable = false)
    private BigDecimal price;

    @Column
    private int capacity;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @OneToMany(mappedBy = "room",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    public void addBooking(LocalDate checkIn, LocalDate checkOut) {
        getBookings().add(new Booking(null, this, checkIn, checkOut));
    }

    public Room(String name, String description, Integer number, BigDecimal price, Integer capacity, Hotel hotel) {
        this.name = name;
        this.description = description;
        this.number = number;
        this.price = price;
        this.capacity = capacity;
        this.hotel = hotel;
    }
}
