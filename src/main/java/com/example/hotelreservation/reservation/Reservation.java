package com.example.hotelreservation.reservation;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String number;
    private String additionalCaveat;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long roomId;
    public Reservation() {

    }

    public Reservation(String firstName, String lastName, String email,
                       String number, String additionalCaveat,
                       LocalDate checkInDate, LocalDate checkOutDate, Long roomId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.number = number;
        this.additionalCaveat = additionalCaveat;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomId = roomId;
    }
}
