package accommodationTypes;

import misc.Features;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import static org.junit.Assert.*;

public class HotelRoomTest {

    HotelRoom room;
    String[] photos = {"empty"};
    HashSet<Features> features = new HashSet<>();

    @Before
    public void setUp() throws Exception {
        features.add(Features.WIFI);
        features.add(Features.BAR);

        room = new HotelRoom(
                "MyHotelRoom",
                photos,
                "A1",
                features,
                1,
                35,
                1,
                "asimakis321"
        );
    }
    @Test
    public void testSetGet(){
        assertEquals(1, (int) room.getFloor());
        assertEquals(35, (int) room.getArea());
        assertEquals(1, (int) room.getBeds());

        room.setFloor(2);
        room.setArea(100);
        room.setBeds(3);

        assertEquals(2, (int) room.getFloor());
        assertEquals(100, (int) room.getArea());
        assertEquals(3, (int) room.getBeds());
    }
}