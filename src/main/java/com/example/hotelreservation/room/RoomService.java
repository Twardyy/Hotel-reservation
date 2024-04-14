package com.example.hotelreservation.room;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    RoomService(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    public RoomResponse save(RoomRequest roomRequest) {
        return roomMapper.roomToRoomResponse(
                roomRepository.save(roomMapper.roomRequestToRoom(roomRequest)));
    }

    public Optional<Room> findRoomByRoomNumber(int roomNumber) {
        return Optional.ofNullable(roomRepository.findByRoomNumber(roomNumber));
    }

    public Optional<RoomResponse> findRoomById(Long id) {
        return roomRepository.findById(id).map(roomMapper::roomToRoomResponse);
    }

    public Optional<RoomResponse> update(Long id, RoomRequest roomRequest) {
        var oldRoom = roomRepository.findById(id);

        if (oldRoom.isPresent()) {
            var newRoom = oldRoom.get();
            newRoom.setRoomNumber(roomRequest.roomNumber());
            newRoom.setPricePerNight(roomRequest.pricePerNight());

            return Optional.of(roomMapper.roomToRoomResponse(roomRepository.save(newRoom)));
        }

        return Optional.empty();
    }
}
