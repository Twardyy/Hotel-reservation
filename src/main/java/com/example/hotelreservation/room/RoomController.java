package com.example.hotelreservation.room;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    public RoomController(final RoomService roomService) {this.roomService = roomService; }

    @PostMapping
    ResponseEntity<RoomResponse> addRoom(@RequestBody RoomRequest createRoomRequest) {
        if(isRoomAlreadyExistsByNumberRoom(createRoomRequest.roomNumber())){
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

    @PatchMapping("/{id}")
    ResponseEntity<RoomResponse> updateRoom(@PathVariable Long id, @RequestBody RoomRequest roomRequest) {
        if(!isRoomAlreadyExistsById(id)) {
            return ResponseEntity.status(NOT_FOUND).build();
        }

        return roomService.update(id, roomRequest)
                .map(roomResponse -> ResponseEntity.ok().body(roomResponse))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    private boolean isRoomAlreadyExistsByNumberRoom(int roomNumber) {
        return roomService.findRoomByRoomNumber(roomNumber).isPresent();
    }

    private boolean isRoomAlreadyExistsById(Long id) {
        return roomService.findRoomById(id).isPresent();
    }

}
