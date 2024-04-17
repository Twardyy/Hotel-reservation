package com.example.hotelreservation.reservation;

import com.example.hotelreservation.UseCase;
import com.example.hotelreservation.room.RoomRequest;
import com.example.hotelreservation.room.RoomResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.ErrorResponse;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpMethod.PATCH;
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

    @Test
    @DisplayName("Should return 200 and get information about reservation")
    void shoudlGetReservation() {
        //given
        var roomRequest = new RoomRequest(21,37);

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
                "Sarah",
                "Wilson",
                "wilsonnny@example.com",
                "331554832",
                "",
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, 2, 26),
                roomResponse.getBody().id()
        );

        //when
        var postReservationResponse = restTemplate.postForEntity(
                prepareUrl("/reservation"),
                reservationRequest,
                ReservationResponse.class
        );

        //then
        assertThat(postReservationResponse.getStatusCode(), equalTo(CREATED));
        assertThat(postReservationResponse.getBody(),notNullValue());
        var reservationBody = postReservationResponse.getBody();
        assertThat(reservationBody.firstName(),equalTo(reservationRequest.firstName()));
        assertThat(reservationBody.lastName(),equalTo(reservationRequest.lastName()));
        assertThat(reservationBody.email(),equalTo(reservationRequest.email()));
        assertThat(reservationBody.number(),equalTo(reservationRequest.number()));
        assertThat(reservationBody.additionalCaveat(),equalTo(reservationRequest.additionalCaveat()));
        assertThat(reservationBody.checkInDate(),equalTo(reservationRequest.checkInDate()));
        assertThat(reservationBody.checkOutDate(),equalTo(reservationRequest.checkOutDate()));
        assertThat(reservationBody.roomId(),equalTo(reservationRequest.roomId()));
        assertThat(reservationBody.roomId(),equalTo(roomResponse.getBody().id()));

        //when
        var reservationId = reservationBody.id();
        var getReservationResponse = restTemplate.getForEntity(
                prepareUrl("/reservation/" + reservationId),
                ReservationResponse.class
        );

        //then
        assertThat(getReservationResponse.getStatusCode(),equalTo(OK));
        assertThat(getReservationResponse.getBody(), equalTo(postReservationResponse.getBody()));
    }

    @Test
    @DisplayName("Should return 404 not found reservation")
    void shouldNotGetReservation() {
        //given
        var id = -1L;

        //when
        var getReservationResponse = restTemplate.getForEntity(
                prepareUrl("/reservation/" + id),
                ReservationResponse.class
        );

        //then
        assertThat(getReservationResponse.getStatusCode(),equalTo(NOT_FOUND));
    }

    @Test
    @DisplayName("Should return 200 and update reservation")
    void shouldUpdateReservation() {
        //given
        var roomRequest = new RoomRequest(35,32);

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
                "Jon",
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

        //given
        var bodyToUpdateRequest = new ReservationRequest(
                "Sandra",
                "DEE",
                "mjones@example.com",
                "123456789",
                "Extra towel",
                LocalDate.of(2024, 4, 18),
                LocalDate.of(2024, 4, 20),
                roomResponse.getBody().id()
        );

        //when
        var updateReservationResponse = restTemplate.exchange(
                prepareUrl("/reservation/" + roomResponse.getBody().id()),
                PATCH,
                createBody(bodyToUpdateRequest),
                ReservationResponse.class
        );

        //then
        assertThat(updateReservationResponse.getStatusCode(), equalTo(OK));
        assertThat(updateReservationResponse.getBody(),notNullValue());
        var updateReservationBody = updateReservationResponse.getBody();
        assertThat(updateReservationBody.firstName(),equalTo(bodyToUpdateRequest.firstName()));
        assertThat(updateReservationBody.lastName(),equalTo(bodyToUpdateRequest.lastName()));
        assertThat(updateReservationBody.email(),equalTo(bodyToUpdateRequest.email()));
        assertThat(updateReservationBody.number(),equalTo(bodyToUpdateRequest.number()));
        assertThat(updateReservationBody.additionalCaveat(),equalTo(bodyToUpdateRequest.additionalCaveat()));
        assertThat(updateReservationBody.checkInDate(),equalTo(bodyToUpdateRequest.checkInDate()));
        assertThat(updateReservationBody.checkOutDate(),equalTo(bodyToUpdateRequest.checkOutDate()));
        assertThat(updateReservationBody.roomId(),equalTo(bodyToUpdateRequest.roomId()));
    }

    @Test
    @DisplayName("Should return 404 not found reservation")
    void shouldNotUpdateReservation() {
        //given
        var roomRequest = new RoomRequest(33,444);

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
        var id =-2L;
        var reservationRequest = new ReservationRequest(
                "Elizabeth",
                "Taylor",
                "eliz@example.com",
                "1112223333",
                "",
                LocalDate.of(2025, 12, 14),
                LocalDate.of(2025, 12, 20),
                roomResponse.getBody().id()
        );

        //when
        var updateReservationResponse = restTemplate.exchange(
                prepareUrl("/reservation/" + id),
                PATCH,
                createBody(reservationRequest),
                ErrorResponse.class
        );
        assertThat(updateReservationResponse.getStatusCode(), equalTo(NOT_FOUND));
    }
}
