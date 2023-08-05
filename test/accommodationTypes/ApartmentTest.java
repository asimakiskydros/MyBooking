package accommodationTypes;

import misc.Features;
import org.junit.Before;
import org.junit.Test;
import java.util.HashSet;
import static org.junit.Assert.*;

public class ApartmentTest {

    Apartment apartment;
    String[] photos = {"empty"};
    HashSet<Features> features = new HashSet<>();

    @Before
    public void setUp() throws Exception{
        features.add(Features.WIFI);
        features.add(Features.PROXIMITY);

        apartment = new Apartment(
                "MyApartment",
                photos,
                "Cool Apartment",
                features,
                "11 Downing Street",
                40,
                2,
                "asimakis321"
        );
    }
    @Test
    public void testSetGet(){
        assertEquals("11 Downing Street", apartment.getAddress());
        assertEquals(40, (int) apartment.getArea());
        assertEquals(2, (int) apartment.getRooms());

        apartment.setAddress("10 Downing Street");
        apartment.setArea(50);
        apartment.setBedrooms(2);
        apartment.setBathrooms(3);

        assertEquals("10 Downing Street", apartment.getAddress());
        assertEquals(50, (int) apartment.getArea());
        assertNotEquals(2, (int) apartment.getBedrooms());
        assertNotEquals(3, (int) apartment.getBathrooms());

        apartment.setRooms(5);
        apartment.setBedrooms(2);
        apartment.setBathrooms(2);
        assertEquals(5, (int) apartment.getRooms());
        assertEquals(2, (int) apartment.getBedrooms());
        assertEquals(2, (int) apartment.getBathrooms());
    }
}