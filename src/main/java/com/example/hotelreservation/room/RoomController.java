package com.example.hotelreservation.room;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.status(CONFLICT).build();
        }

        return ResponseEntity.status(CREATED).body(roomService.save(createRoomRequest));
    }

    @GetMapping("/id")
    RoomResponse fetchInformationAboutRoom(@PathVariable Long id) {return roomService.findRoomById(id); }
    private boolean isRoomAlreadyExists(int roomNumber) {
        return roomService.findRoomByRoomNumber(roomNumber).isPresent();
    }

}
