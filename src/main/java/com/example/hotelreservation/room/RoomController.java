package com.example.hotelreservation.room;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;

import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    public RoomController(final RoomService roomService) {this.roomService = roomService; }

    @PostMapping
    ResponseEntity<RoomResponse> addRoom(@RequestBody RoomRequest createRoomRequest) {
        if(isRoomAlreadyExists(createRoomRequest.roomNumber())){
            return ResponseEntity.status(CONFLICT).build();
        }

        return ResponseEntity.status(CREATED).body(roomService.save(createRoomRequest));
    }

    @GetMapping("/{id}")
    ResponseEntity<RoomResponse> fetchInformationAboutRoom(@PathVariable Long id) {
        return roomService.findRoomById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    private boolean isRoomAlreadyExists(int roomNumber) {
        return roomService.findRoomByRoomNumber(roomNumber).isPresent();
    }

}
