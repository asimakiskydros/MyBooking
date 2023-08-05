package accommodationTypes;

import misc.Features;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Objects;

import static org.junit.Assert.*;

public class HotelTest {

    Hotel hotel;
    String[] photos = {"empty"};
    HashSet<Features> features = new HashSet<>();


    @Before
    public void setUp() throws Exception {
        features.add(Features.WIFI);
        features.add(Features.ELEVATOR);

        hotel = new Hotel(
                "MyHotel",
                photos,
                "Awesome Hotel",
                features,
                new HashSet<>(),
                "11 Downing Street",
                5,
                "asimakis321"
        );
    }
    @Test
    public void testSetGet(){
        assertEquals("11 Downing Street", hotel.getAddress());
        assertEquals(5, (int) hotel.getFloors());

        hotel.setAddress("10 Downing Street");
        hotel.setFloors(4);

        assertEquals("10 Downing Street", hotel.getAddress());
        assertEquals(4, (int) hotel.getFloors());
    }
    @Test
    public void RoomAddRemoveSetGetTest(){
        HashSet<HotelRoom> rooms = new HashSet<>();
        HotelRoom testRoom = new HotelRoom(
                "MyHotelRoom",
                photos,
                "A1",
                features,
                1,
                35,
                1,
                "asimakis321"
        );
        rooms.add(testRoom);

        hotel.setRooms(rooms);
        for(HotelRoom room : hotel.getRooms()) {
            assertEquals("MyHotelRoom", room.getName());
            assertEquals("A1", room.getDescription());
        }
        hotel.addRoom(new HotelRoom(
                "MyHotelRoom1",
                photos,
                "A2",
                features,
                1,
                35,
                1,
                "asimakis321"
        ));
        hotel.addRoom(new HotelRoom(
                "MyHotelRoom2",
                photos,
                "B1",
                features,
                1,
                35,
                1,
                "asimakis321"
        ));
        hotel.removeRoom(testRoom);

        boolean room1Exists = false, room2Exists = false, roomTestExists = false;
        for(HotelRoom room : hotel.getRooms()){
            if(Objects.equals(room.getName(), "MyHotelRoom1"))
                room1Exists = true;
            if(Objects.equals(room.getName(), "MyHotelRoom2"))
                room2Exists = true;
            if(Objects.equals(room.getName(), "MyHotelRoom"))
                roomTestExists = true;
        }

        assertTrue(room1Exists);
        assertTrue(room2Exists);
        assertFalse(roomTestExists);
    }
}