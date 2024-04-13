package com.example.hotelreservation.reservation;

import org.springframework.stereotype.Service;

@Service
class ReservationService {
    private final ReservationRepository reservationRepository;

    ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

}
