package org.example.bookingservice.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.bookingservice.domain.Booking;
import org.springframework.data.jpa.domain.Specification;

public interface BookingSpecifications {

    static Specification<Booking> byUser(Long userId) {

        return(Root<Booking> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("userId"), userId);
    }
}
