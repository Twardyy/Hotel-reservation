package com.example.hotelreservation.room;

import org.springframework.stereotype.Component;

@Component
class RoomMapper {
    RoomResponse roomToRoomResponse(Room room) {
        return new RoomResponse(room.getId(), room.getRoomNumber(), room.getPricePerNight());
    }
    Room roomRequestToRoom(RoomRequest roomRequest) {
        return new Room(roomRequest.roomNumber(),roomRequest.pricePerNight());
    }
}
