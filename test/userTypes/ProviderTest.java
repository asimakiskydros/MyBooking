package userTypes;

import accommodationTypes.*;
import data.Database;
import data.Initializer;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.util.HashSet;

import static org.junit.Assert.*;

public class ProviderTest {

    Provider provider;

    @Before
    public void setUp() throws Exception {
        provider = new Provider(
                "asimakis123",
                "123",
                "Asimakis Kydros",
                "Spiti mou",
                "6940404040",
                "11 Downing Street",
                true
        );
    }
    @Test
    public void testSetGet(){
        assertEquals("Spiti mou", provider.getAddress());
        assertEquals("11 Downing Street", provider.getAddressHQ());

        HashSet<String> tempAccommodations = new HashSet<>();
        tempAccommodations.add("My Apartment");
        provider.setOwnedAccommodations(tempAccommodations);
        provider.setAddress("Spiti sou");
        provider.setAddressHQ("10 Downing Street");

        assertEquals("Spiti sou", provider.getAddress());
        assertEquals("10 Downing Street", provider.getAddressHQ());
        for(String accommodation : provider.getOwnedAccommodations())
            assertEquals("My Apartment", accommodation);
    }
    @Test
    public void AddAccommodationTest(){
        Database tempDatabase = new Database("test" + File.separator + "backUpFiles" + File.separator);
        new Initializer(tempDatabase);

        Accommodation tempApartment = new Apartment();
        assertTrue(provider.addAccommodation(tempApartment, tempDatabase));
        assertTrue(provider.getOwnedAccommodations().contains(""));
    }
    @Test
    public void removeAccommodationTest(){
        Database tempDatabase = new Database("test" + File.separator + "backUpFiles" + File.separator);
        new Initializer(tempDatabase);

        Accommodation tempApartment = new Apartment();
        assertTrue(provider.addAccommodation(tempApartment, tempDatabase));
        Customer tempCustomer = new Customer(
                "test",
                "123",
                "test",
                "test",
                "test",
                true
        );
        assertTrue(tempCustomer.addReservation(tempApartment, 20210101, 20210104, tempDatabase));
        assertTrue(provider.removeAccommodation(tempApartment, tempDatabase));

        assertFalse(provider.getOwnedAccommodations().contains(""));
    }
    @Test
    public void TotalReservationsAndCancellationsTest(){
        Database tempDatabase = new Database("test" + File.separator + "backUpFiles" + File.separator);
        new Initializer(tempDatabase);

        Customer tempCustomer = new Customer(
                "test",
                "123",
                "test",
                "test",
                "test",
                true
        );
        Accommodation tempApartment = new Apartment();
        provider.addAccommodation(tempApartment, tempDatabase);
        tempCustomer.addReservation(tempApartment, 20210101, 20210104, tempDatabase);

        assertEquals(1, (int) provider.getTotalReservations(tempDatabase));
        assertEquals(0, (int) provider.getTotalCancellations(tempDatabase));

        tempCustomer.cancelReservation(tempApartment, tempDatabase);
        assertEquals(0, (int) provider.getTotalReservations(tempDatabase));
        assertEquals(1, (int) provider.getTotalCancellations(tempDatabase));
    }
}