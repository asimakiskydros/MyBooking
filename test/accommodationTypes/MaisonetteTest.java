package accommodationTypes;

import misc.Features;
import org.junit.Before;
import org.junit.Test;
import java.util.HashSet;
import static org.junit.Assert.*;

public class MaisonetteTest {

    Maisonette maisonette;
    String[] photos = {"empty"};
    HashSet<Features> features = new HashSet<>();

    @Before
    public void setUp() throws Exception{
        features.add(Features.WIFI);
        features.add(Features.PROXIMITY);

        maisonette = new Maisonette(
                "MyApartment",
                photos,
                "Cool Apartment",
                features,
                "11 Downing Street",
                40,
                2,
                2,
                "asimakis321"
        );
    }
    @Test
    public void testSetGet(){
        assertEquals("11 Downing Street", maisonette.getAddress());
        assertEquals(40, (int) maisonette.getArea());
        assertEquals(2, (int) maisonette.getRooms());
        assertEquals(2, (int) maisonette.getFloors());

        maisonette.setAddress("10 Downing Street");
        maisonette.setArea(50);
        maisonette.setBedrooms(2);
        maisonette.setBathrooms(3);
        maisonette.setFloors(3);

        assertEquals("10 Downing Street", maisonette.getAddress());
        assertEquals(50, (int) maisonette.getArea());
        assertEquals(3, (int) maisonette.getFloors());
        assertNotEquals(2, (int) maisonette.getBedrooms());
        assertNotEquals(3, (int) maisonette.getBathrooms());

        maisonette.setRooms(5);
        maisonette.setBedrooms(2);
        maisonette.setBathrooms(2);
        assertEquals(5, (int) maisonette.getRooms());
        assertEquals(2, (int) maisonette.getBedrooms());
        assertEquals(2, (int) maisonette.getBathrooms());
    }
}