package com.example.hotelreservation.room;

public record RoomRequest(
        int roomNumber,
        double pricePerNight
) {
}
