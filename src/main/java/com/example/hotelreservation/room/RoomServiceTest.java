package com.example.hotelreservation.room;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RoomServiceTest {
    @InjectMocks
    private RoomService roomService;

    @Mock
    private RoomMapper roomMapper;

    @Mock
    private RoomController roomController;

    @Mock
    private RoomRepository roomRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void shouldSaveRoom(){
        //given
        RoomRequest roomRequest = new RoomRequest(101,150);
        Room room = new Room(101,150);
        RoomResponse expectedResponse = new RoomResponse(1L,101,150);

        when(roomMapper.roomRequestToRoom(roomRequest)).thenReturn(room);
        when(roomRepository.save(room)).thenReturn(room);
        when(roomMapper.roomToRoomResponse(room)).thenReturn(expectedResponse);

        //when
        RoomResponse result = roomService.save(roomRequest);

        // then
        assertEquals(expectedResponse, result);
        verify(roomMapper).roomRequestToRoom(roomRequest);
        verify(roomRepository).save(room);
        verify(roomMapper).roomToRoomResponse(room);
    }

    @Test
    public void shouldFindRoomByRoomNumber() {
        //given
        var roomNumber = 106;
        Room room = new Room(101,30);

        when(roomRepository.findByRoomNumber(roomNumber)).thenReturn(room);

        //when
        var result = roomService.findRoomByRoomNumber(roomNumber);

        //given
        assertEquals(Optional.of(room), result);
        verify(roomRepository).findByRoomNumber(roomNumber);
    }

    @Test
    public void shouldFindRoomById() {
        //given
        Long roomId = 1L;
        Room room = new Room(101, 150);
        RoomResponse expectedResponse = new RoomResponse(roomId, 101, 150);

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(roomMapper.roomToRoomResponse(room)).thenReturn(expectedResponse);

        //when
        Optional<RoomResponse> result = roomService.findRoomById(roomId);

        // then
        assertEquals(Optional.of(expectedResponse), result);
        verify(roomRepository).findById(roomId);
        verify(roomMapper).roomToRoomResponse(room);
    }

    @Test
    public void shouldUpdateRoom() {
        //given
        var id = 1L;
        var roomRequest = new RoomRequest(543, 12);
        var roomResponse = new RoomResponse(id, 44, 33);

        when(roomService.update(id,roomRequest)).thenReturn(Optional.of(roomResponse));

        //when
        var response = roomController.updateRoom(id,roomRequest).getBody();

        //then
        assertEquals(response,roomResponse);
        //assertEquals(response.getStatusCode(), HttpStatus.OK);
        //assertEquals(response.getBody(), roomResponse);
    }
}
