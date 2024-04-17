package com.example.hotelreservation.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r WHERE r.roomId = :roomId AND (:checkInDate BETWEEN r.checkInDate AND r.checkOutDate OR :checkOutDate BETWEEN r.checkInDate AND r.checkOutDate)")
    List<Reservation> findByRoomIdAndDates(Long roomId, LocalDate checkInDate, LocalDate checkOutDate);
}
