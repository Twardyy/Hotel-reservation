package com.example.hotelreservation.room;

import com.example.hotelreservation.UseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.*;

class roomTest extends UseCase {
    @Test
    @DisplayName("Should return 201 and create new room")
    void shoudCreateRoom() {
        //given
        var roomRequest = new RoomRequest(1,32);

        //when
        var response = restTemplate.postForEntity(
                prepareUrl("/room"),
                roomRequest,
                RoomResponse.class
        );

        //then
        assertThat(response.getStatusCode(), equalTo(CREATED));
        assertThat(response.getBody(),notNullValue());
        assertThat(response.getBody().roomNumber(), equalTo(roomRequest.roomNumber()));
        assertThat(response.getBody().pricePerNight(), equalTo(roomRequest.pricePerNight()));
    }

    @Test
    @DisplayName("Should return 409 Conflict when room is already exist")
    void shouldNotCreateRoomBecauseTheNumberRoomIsAlreadyExist() {
        //given
        var roomRequest = new RoomRequest(5,23);
        var roomWithSameNumberRequest = new RoomRequest(5,422);

        //when
        var newRoomResponse = restTemplate.postForEntity(
                prepareUrl("/room"),
                roomRequest,
                RoomResponse.class
        );

        var existingRoomResponse = restTemplate.postForEntity(
                prepareUrl("/room"),
                roomWithSameNumberRequest,
                ErrorResponse.class
        );

        //then
        assertThat(newRoomResponse.getStatusCode(), equalTo(CREATED));
        assertThat(newRoomResponse.getBody(),notNullValue());
        assertThat(newRoomResponse.getBody().roomNumber(), equalTo(roomRequest.roomNumber()));
        assertThat(newRoomResponse.getBody().pricePerNight(), equalTo(roomRequest.pricePerNight()));
        assertThat(existingRoomResponse.getStatusCode(), equalTo(CONFLICT));
    }

    @Test
    @DisplayName("Should return 200 and find room")
    void shouldFindRoom() {
        //given
        var roomRequest = new RoomRequest(3,34);

        //when
        var postRoomResponse = restTemplate.postForEntity(
                prepareUrl("/room"),
                roomRequest,
                RoomResponse.class
        );

        assertThat(postRoomResponse.getStatusCode(), equalTo(CREATED));

        var roomId = postRoomResponse.getBody().id();
        var getRoomResponse = restTemplate.getForEntity(
                prepareUrl("/room/" + roomId),
                RoomResponse.class
        );

        //then
        assertThat(postRoomResponse.getStatusCode(), equalTo(CREATED));
        assertThat(getRoomResponse.getStatusCode(), equalTo(OK));
        assertThat(postRoomResponse.getBody().roomNumber(), equalTo(roomRequest.roomNumber()));
        assertThat(postRoomResponse.getBody().pricePerNight(), equalTo(roomRequest.pricePerNight()));
        assertThat(postRoomResponse.getBody().roomNumber(), equalTo(getRoomResponse.getBody().roomNumber()));
        assertThat(postRoomResponse.getBody().pricePerNight(), equalTo(getRoomResponse.getBody().pricePerNight()));
    }

    @Test
    @DisplayName("Should return 401 not found room")
    void shouldNotFoundRoom(){
        //given
        var id = 200L;

        //when
        var response = restTemplate.getForEntity(
                prepareUrl("/room/" + id),
                ErrorResponse.class
        );

        //then
        assertThat(response.getStatusCode(), equalTo(NOT_FOUND));
    }
}
