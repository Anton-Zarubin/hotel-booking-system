package org.example.hotelservice.repository;

import jakarta.persistence.criteria.*;
import org.example.hotelservice.domain.Booking;
import org.example.hotelservice.domain.Hotel;
import org.example.hotelservice.domain.Room;
import org.example.hotelservice.dto.room.RoomFilter;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public interface RoomSpecifications {

    static Specification<Room> withFilter(RoomFilter roomFilter) {

        return Specification.where(byPrice(roomFilter.getMinPrice(), roomFilter.getMaxPrice()))
                .and(byCapacity(roomFilter.getCapacity()))
                .and(byHotelId(roomFilter.getHotelId()))
                .and(byDates(roomFilter.getCheckIn(), roomFilter.getCheckOut()));
    }

    static Specification<Room> byPrice(BigDecimal minPrice, BigDecimal maxPrice) {

        if(minPrice == null && maxPrice == null) return null;

        if(minPrice == null) {
            return(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                    criteriaBuilder.le(root.get("price"), maxPrice);
        } else if(maxPrice == null) {
            return(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                    criteriaBuilder.ge(root.get("price"), minPrice);
        } else {
            return(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                    criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
        }
    }

    static Specification<Room> byCapacity(Integer capacity) {

        if(capacity != null) return(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("capacity"), capacity);

        return null;
    }

    static Specification<Room> byHotelId(Long hotelId) {

        if(hotelId != null) return(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Join<Hotel, Room> hotelRoomJoin = root.join("hotel");
            return criteriaBuilder.equal(hotelRoomJoin.get("id"), hotelId);
        };

        return null;
    }

    static Specification<Room> byDates(LocalDate checkIn, LocalDate checkOut) {

        if(checkIn == null || checkOut == null) return null;

        return (Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {

            Subquery<Booking> sub = Objects.requireNonNull(query).subquery(Booking.class);
            Root<Booking> bookingRoot = sub.from(Booking.class);
            Join<Booking, Room> bookingRoomJoin = bookingRoot.join("room");

            Predicate datesOverlap = criteriaBuilder.or(
                    criteriaBuilder.between(bookingRoot.get("checkIn"), checkIn, checkOut),
                    criteriaBuilder.between(bookingRoot.get("checkOut"), checkIn, checkOut),
                    criteriaBuilder.and(
                            criteriaBuilder.lessThan(bookingRoot.get("checkIn"), checkIn),
                            criteriaBuilder.greaterThan(bookingRoot.get("checkOut"), checkOut)
                    )
            );

            sub.select(bookingRoot);
            sub.where(criteriaBuilder.and(criteriaBuilder.equal(bookingRoomJoin, root), datesOverlap));

            return criteriaBuilder.not(criteriaBuilder.exists(sub));
        };
    }
}
