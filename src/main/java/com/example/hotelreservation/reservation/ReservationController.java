package com.example.hotelreservation.reservation;

import com.example.hotelreservation.room.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("reservation")
public class ReservationController {
    private final ReservationService reservationService;
    private final RoomService roomService;

    public ReservationController(final ReservationService reservationService, RoomService roomService) {
        this.reservationService = reservationService;
        this.roomService = roomService;
    }

    @PostMapping
    ResponseEntity<ReservationResponse> addReservation(@RequestBody ReservationRequest createReservationRequest) {
        if(!isRequestCorrect(createReservationRequest)) {
            return ResponseEntity.status(BAD_REQUEST).build();
        }

        if(!isRoomExists(createReservationRequest.roomId())) {
            return ResponseEntity.status(NOT_FOUND).build();
        }

        if(!isRoomAvailable(createReservationRequest.roomId(),
                createReservationRequest.checkInDate(), createReservationRequest.checkOutDate())) {
            return ResponseEntity.status(CONFLICT).build();
        }

        return ResponseEntity.status(CREATED).body(reservationService.save(createReservationRequest));
    }

    @GetMapping("/{id}")
    ResponseEntity<ReservationResponse> fetchInformationAboutReservation(@PathVariable Long id) {
        return reservationService.findReservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    ResponseEntity<ReservationResponse> updateReservation(
            @PathVariable Long id, @RequestBody ReservationRequest reservationRequest) {
        return reservationService.update(id,reservationRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private boolean isRequestCorrect(ReservationRequest reservationRequest){
        if(reservationRequest.firstName().isEmpty() ||
                reservationRequest.lastName().isEmpty() ||
                reservationRequest.email().isEmpty() ||
                reservationRequest.number().isEmpty() ||
                reservationRequest.checkInDate() == null ||
                reservationRequest.checkOutDate() == null ||
                reservationRequest.roomId() == null
        ) {
            return false;
        }
        if (!reservationRequest.checkInDate().isBefore(reservationRequest.checkOutDate())) {
            return false;
        }

        return true;
    }

    private boolean isRoomExists(Long roomId) {
        return roomService.findRoomById(roomId).isPresent();
    }

    private boolean isRoomAvailable(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        return reservationService.isRoomOccupied(roomId, checkInDate, checkOutDate);
    }

}
