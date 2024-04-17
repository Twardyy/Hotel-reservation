package com.example.hotelreservation.reservation;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Optional<ReservationResponse> findReservationById(Long id) {
        return reservationRepository.findById(id).
                map(reservationMapper::reservationToReservationResponse);
    }

    public Optional<ReservationResponse> update(Long id, ReservationRequest reservationRequest) {
        var oldReservation = reservationRepository.findById(id);

        if(oldReservation.isPresent()) {
            var newReservation = oldReservation.get();
            newReservation.setFirstName(reservationRequest.firstName());
            newReservation.setLastName(reservationRequest.lastName());
            newReservation.setEmail(reservationRequest.email());
            newReservation.setNumber(reservationRequest.number());
            newReservation.setAdditionalCaveat(reservationRequest.additionalCaveat());
            newReservation.setCheckInDate(reservationRequest.checkInDate());
            newReservation.setCheckOutDate(reservationRequest.checkOutDate());
            newReservation.setRoomId(reservationRequest.roomId());

            return Optional.of(reservationMapper.
                    reservationToReservationResponse(reservationRepository.save(newReservation)));
        }

        return Optional.empty();
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(reservationMapper::reservationToReservationResponse)
                .collect(Collectors.toList());
    }

    public boolean isRoomOccupied(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        return reservationRepository.findByRoomIdAndDates(roomId, checkInDate, checkOutDate)
                .isEmpty();
    }

}
