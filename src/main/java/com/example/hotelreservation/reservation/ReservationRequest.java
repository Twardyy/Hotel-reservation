package com.example.hotelreservation.reservation;

import java.time.LocalDate;

public record ReservationRequest(
    String firstName,
    String lastName,
    String email,
    String number,
    String additionalCaveat,
    LocalDate checkInDate,
    LocalDate checkOutDate,
    Long roomId) {
}

