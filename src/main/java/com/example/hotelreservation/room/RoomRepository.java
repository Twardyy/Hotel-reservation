package com.example.hotelreservation.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RoomRepository extends JpaRepository<Room, Long> {

    Room findByRoomNumber(int roomNumber);
}
