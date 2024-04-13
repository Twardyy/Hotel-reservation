package com.example.hotelreservation.room;

public record RoomResponse (
    Long id,
    int roomNumber,
    double pricePerNight) {
}
