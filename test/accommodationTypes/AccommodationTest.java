package accommodationTypes;

import misc.Features;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

public class AccommodationTest {

    Accommodation accommodation;
    String[] photos = {"empty"};
    HashSet<Features> features = new HashSet<>();

    @Before
    public void setUp() throws Exception{
        features.add(Features.WIFI);
        features.add(Features.SPA);

        accommodation = new Accommodation(
                "MyAccommodation",
                photos,
                "This is my favorite accommodation",
                features,
                "asimakis321"
        );
    }
    @Test
    public void testSetGet(){
        assertEquals("MyAccommodation", accommodation.getName());
        assertEquals("This is my favorite accommodation", accommodation.getDescription());
        assertEquals("asimakis321", accommodation.getOwner());
        for(String photo : accommodation.getPhotos())
            assertEquals("empty", photo);
        assertTrue(accommodation.getFeatures().contains(Features.WIFI));
        assertTrue(accommodation.getFeatures().contains(Features.SPA));
        assertFalse(accommodation.getFeatures().contains(Features.BAR));

        accommodation.setName("YourAccommodation");
        accommodation.setDescription("Ayy me too dude");
        String[] photos = {"cute_cat_pic.jpg"};
        accommodation.setPhotos(photos);
        accommodation.addFeature(Features.BAR);

        assertEquals("YourAccommodation", accommodation.getName());
        assertEquals("Ayy me too dude", accommodation.getDescription());
        for(String photo : accommodation.getPhotos())
            assertEquals("cute_cat_pic.jpg", photo);
        assertTrue(accommodation.getFeatures().contains(Features.WIFI));
        assertTrue(accommodation.getFeatures().contains(Features.SPA));
        assertTrue(accommodation.getFeatures().contains(Features.BAR));
    }
    @Test
    public void addReservationTest(){
        assertTrue(accommodation.addReservation(20210101, 20210104));
        assertEquals(1, (int) accommodation.getTimesReserved());
        assertEquals(0, (int) accommodation.getTimesCancelled());
    }
    @Test
    public void removeReservationTest(){
        assertTrue(accommodation.addReservation(20210101, 20210104));

        assertTrue(accommodation.removeReservation(20210101, 20210104));
        assertEquals(0, (int) accommodation.getTimesReserved());
        assertEquals(1, (int) accommodation.getTimesCancelled());
    }
}