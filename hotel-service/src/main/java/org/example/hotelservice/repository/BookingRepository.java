package org.example.hotelservice.repository;

import org.example.hotelservice.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select not exists (select 1 from Booking b join Room r on r.id = b.room.id where r.id = ?1 and " +
            "((b.checkIn < ?2 and b.checkOut > ?3) or (b.checkIn between ?2 and ?3) or (b.checkOut between ?2 and ?3)))")
    boolean isDatesAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut);
}
