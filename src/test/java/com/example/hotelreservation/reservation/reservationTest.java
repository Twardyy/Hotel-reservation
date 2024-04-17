package com.example.hotelreservation.reservation;

import com.example.hotelreservation.UseCase;
import com.example.hotelreservation.room.RoomRequest;
import com.example.hotelreservation.room.RoomResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.*;

class reservationTest extends UseCase {
    @Test
    @DisplayName("Should return 201 and create new reservation ")
    void shouldCreateReservation() {
        //given
        var roomRequest = new RoomRequest(10,32);

        //when
        var roomResponse = restTemplate.postForEntity(
                prepareUrl("/room"),
                roomRequest,
                RoomResponse.class
        );

        //then
        assertThat(roomResponse.getStatusCode(), equalTo(CREATED));
        assertThat(roomResponse.getBody(),notNullValue());
        assertThat(roomResponse.getBody().roomNumber(), equalTo(roomRequest.roomNumber()));
        assertThat(roomResponse.getBody().pricePerNight(), equalTo(roomRequest.pricePerNight()));

        //given
        var reservationRequest = new ReservationRequest(
                "Michael",
                "Jones",
                "mjones@example.com",
                "123456789",
                "Extra towel",
                LocalDate.of(2024, 4, 18),
                LocalDate.of(2024, 4, 20),
                roomResponse.getBody().id()
        );

        //when
        var reservationResponse = restTemplate.postForEntity(
                prepareUrl("/reservation"),
                reservationRequest,
                ReservationResponse.class
        );

        //then
        assertThat(reservationResponse.getStatusCode(), equalTo(CREATED));
        assertThat(reservationResponse.getBody(),notNullValue());
        var reservationBody = reservationResponse.getBody();
        assertThat(reservationBody.firstName(),equalTo(reservationRequest.firstName()));
        assertThat(reservationBody.lastName(),equalTo(reservationRequest.lastName()));
        assertThat(reservationBody.email(),equalTo(reservationRequest.email()));
        assertThat(reservationBody.number(),equalTo(reservationRequest.number()));
        assertThat(reservationBody.additionalCaveat(),equalTo(reservationRequest.additionalCaveat()));
        assertThat(reservationBody.checkInDate(),equalTo(reservationRequest.checkInDate()));
        assertThat(reservationBody.checkOutDate(),equalTo(reservationRequest.checkOutDate()));
        assertThat(reservationBody.roomId(),equalTo(reservationRequest.roomId()));
        assertThat(reservationBody.roomId(),equalTo(roomResponse.getBody().id()));
    }

    @Test
    @DisplayName("Should return 400 invalid request body")
    void shouldNotCreateReservationBecauseInvalidRequest(){
        //given
        var roomRequest = new RoomRequest(11,332);

        //when
        var roomResponse = restTemplate.postForEntity(
                prepareUrl("/room"),
                roomRequest,
                RoomResponse.class
        );

        //then
        assertThat(roomResponse.getStatusCode(), equalTo(CREATED));
        assertThat(roomResponse.getBody(),notNullValue());
        assertThat(roomResponse.getBody().roomNumber(), equalTo(roomRequest.roomNumber()));
        assertThat(roomResponse.getBody().pricePerNight(), equalTo(roomRequest.pricePerNight()));

        //given
        var reservationRequest = new ReservationRequest(
                "Elizabeth",
                "Smith",
                "",
                "123456789",
                "Put out an extra bed",
                LocalDate.of(2024, 5, 19),
                LocalDate.of(2024, 5, 20),
                roomResponse.getBody().id()
        );

        //when
        var reservationResponse = restTemplate.postForEntity(
                prepareUrl("/reservation"),
                reservationRequest,
                ReservationResponse.class
        );

        //then
        assertThat(reservationResponse.getStatusCode(), equalTo(BAD_REQUEST));
    }

    @Test
    @DisplayName("Should return 404 no room with given id")
    void shouldNotCreateReservationBecauseNoRoom() {
        //given
        var reservationRequest = new ReservationRequest(
                "David",
                "Taylor",
                "mdacidooo@wp.com",
                "331122312",
                "",
                LocalDate.of(2024, 4, 20),
                LocalDate.of(2024, 4, 21),
                -2L
        );

        //when
        var reservationResponse = restTemplate.postForEntity(
                prepareUrl("/reservation"),
                reservationRequest,
                ReservationResponse.class
        );

        //then
        assertThat(reservationResponse.getStatusCode(), equalTo(NOT_FOUND));
    }

    @Test
    @DisplayName("Should return 409 room occupied")
    void shouldNotCreateReservationBecauseRoomIsOccupied() {
        //given
        var roomRequest = new RoomRequest(15,39);

        //when
        var roomResponse = restTemplate.postForEntity(
                prepareUrl("/room"),
                roomRequest,
                RoomResponse.class
        );

        //then
        assertThat(roomResponse.getStatusCode(), equalTo(CREATED));
        assertThat(roomResponse.getBody(),notNullValue());
        assertThat(roomResponse.getBody().roomNumber(), equalTo(roomRequest.roomNumber()));
        assertThat(roomResponse.getBody().pricePerNight(), equalTo(roomRequest.pricePerNight()));

        //given
        var emilyReservationRequest = new ReservationRequest(
                "Emily",
                "Smith",
                "soosmithyy@example.com",
                "111111112",
                "Crying baby",
                LocalDate.of(2025, 1, 18),
                LocalDate.of(2025, 3, 20),
                roomResponse.getBody().id()
        );

        //when
        var emilyReservationResponse = restTemplate.postForEntity(
                prepareUrl("/reservation"),
                emilyReservationRequest,
                ReservationResponse.class
        );

        //then
        assertThat(emilyReservationResponse.getStatusCode(), equalTo(CREATED));
        assertThat(emilyReservationResponse.getBody(),notNullValue());
        var emilyReservationBody = emilyReservationResponse.getBody();
        assertThat(emilyReservationBody.firstName(),equalTo(emilyReservationRequest.firstName()));
        assertThat(emilyReservationBody.lastName(),equalTo(emilyReservationRequest.lastName()));
        assertThat(emilyReservationBody.email(),equalTo(emilyReservationRequest.email()));
        assertThat(emilyReservationBody.number(),equalTo(emilyReservationRequest.number()));
        assertThat(emilyReservationBody.additionalCaveat(),equalTo(emilyReservationRequest.additionalCaveat()));
        assertThat(emilyReservationBody.checkInDate(),equalTo(emilyReservationRequest.checkInDate()));
        assertThat(emilyReservationBody.checkOutDate(),equalTo(emilyReservationRequest.checkOutDate()));
        assertThat(emilyReservationBody.roomId(),equalTo(emilyReservationRequest.roomId()));
        assertThat(emilyReservationBody.roomId(),equalTo(roomResponse.getBody().id()));

        //given
        var jenniferReservationRequest = new ReservationRequest(
                "Jennifer",
                "Miller",
                "soosmithyy@example.com",
                "111111112",
                "Crying baby",
                LocalDate.of(2025, 1, 18),
                LocalDate.of(2025, 3, 20),
                roomResponse.getBody().id()
        );

        //when
        var jenniferReservationResponse = restTemplate.postForEntity(
                prepareUrl("/reservation"),
                jenniferReservationRequest,
                ReservationResponse.class
        );
        assertThat(jenniferReservationResponse.getStatusCode(), equalTo(CONFLICT));
    }
}
