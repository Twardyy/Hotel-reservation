package com.example.hotelreservation.reservation;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    ReservationService(ReservationRepository reservationRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        return reservationMapper.reservationToReservationResponse(
                reservationRepository.save(reservationMapper.reservationRequestToReservation(reservationRequest)));
    }

    public boolean isRoomOccupied(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        var reservations = reservationRepository.findByRoomIdAndDates(roomId, checkInDate, checkOutDate);
        return reservations.isEmpty();
    }

}
