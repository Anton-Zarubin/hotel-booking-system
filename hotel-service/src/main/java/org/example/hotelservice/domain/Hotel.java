package org.example.hotelservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;

    @Column(name = "distance_from_the_city_center")
    private Integer distanceFromCenter;

    @Column
    private double rating;

    @Column(name = "number_of_rating")
    private int numberOfRating;

    @OneToMany(mappedBy = "hotel",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    List<Room> rooms = new ArrayList<>();

    public void addRoom(Room room) {
        room.setHotel(this);
        rooms.add(room);
    }

    public Hotel(String name, String city, String address, Integer distanceFromCenter) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.distanceFromCenter = distanceFromCenter;
    }
}