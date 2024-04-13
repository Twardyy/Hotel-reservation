package com.example.hotelreservation.room;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    public RoomController(final RoomService roomService) {this.roomService = roomService; }

    @PostMapping
    ResponseEntity<?> addRoom(@RequestBody RoomRequest createRoomRequest) {
        if(isRoomAlreadyExists(createRoomRequest.roomNumber())){
            return ResponseEntity.status(CONFLICT)
                    .body("Room with number: " + createRoomRequest.roomNumber() + " was added earlier");
        }

        return ResponseEntity.status(CREATED).body(roomService.save(createRoomRequest));
    }
    private boolean isRoomAlreadyExists(int roomNumber) {
        return roomService.findRoomByRoomNumber(roomNumber).isPresent();
    }

}
