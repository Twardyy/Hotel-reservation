package com.example.hotelreservation.reservation;

import org.springframework.stereotype.Component;

@Component
class ReservationMapper {
    Reservation reservationRequestToReservation(ReservationRequest reservationRequest) {
        return new Reservation(reservationRequest.firstName(), reservationRequest.lastName(),
                reservationRequest.email(), reservationRequest.number(),
                reservationRequest.additionalCaveat(), reservationRequest.checkInDate(),
                reservationRequest.checkOutDate(), reservationRequest.roomId());
    }

    ReservationResponse reservationToReservationResponse(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getFirstName(),
                reservation.getLastName(), reservation.getEmail(),
                reservation.getNumber(), reservation.getAdditionalCaveat(),
                reservation.getCheckInDate(),reservation.getCheckOutDate(),
                reservation.getRoomId());
    }

}
