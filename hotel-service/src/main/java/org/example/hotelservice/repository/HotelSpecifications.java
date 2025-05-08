package org.example.hotelservice.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.hotelservice.domain.Hotel;
import org.example.hotelservice.dto.hotel.HotelFilter;
import org.springframework.data.jpa.domain.Specification;

public interface HotelSpecifications {

    static Specification<Hotel> withFilter(HotelFilter hotelFilter) {

        return Specification.where(byCity(hotelFilter.getCity()))
                .and(byDistanceFromCenter(hotelFilter.getDistanceFromCenter()))
                .and(byRating(hotelFilter.getRating()));
    }

    static Specification<Hotel> byCity(String city) {

        if(city != null) return(Root<Hotel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("city"), city);

        return null;
    }

    static Specification<Hotel> byDistanceFromCenter(Integer distanceFromCenter) {

        if(distanceFromCenter != null) return(Root<Hotel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.le(root.get("distanceFromCenter"), distanceFromCenter);

        return null;
    }

    static Specification<Hotel> byRating(Double rating) {

        if(rating != null) return(Root<Hotel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.ge(root.get("rating"), rating);

        return null;
    }
}
