package com.example.hotelreservation.room;

import org.springframework.stereotype.Service;

@Service
class RoomService {
    private final RoomRepository roomRepository;

    RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
}
