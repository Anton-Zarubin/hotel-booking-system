package org.example.hotelservice.exception;

public class UnavailableDatesException extends RuntimeException {

    public UnavailableDatesException() {
        super("It's not possible to book a room for the specified dates");
    }
}
