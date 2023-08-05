package userTypes;

import accommodationTypes.*;
import data.Database;
import data.Initializer;
import misc.MyPair;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import static org.junit.Assert.*;

public class CustomerTest {

    Customer customer;

    @Before
    public void setUp() throws Exception{
        customer = new Customer(
                "asimakis123",
                "123",
                "Asimakis Kydros",
                "Spiti mou",
                "6940404040",
                true
        );
    }
    @Test
    public void testSetGet(){
        HashMap<MyPair, String> tempReservations = new HashMap<>();
        HashSet<String> tempCancellations = new HashSet<>();

        tempReservations.put(new MyPair(20210101, 20210104), "MyAccommodation");
        tempCancellations.add("MyCancellation");

        customer.setReservations(tempReservations);
        customer.setCancellations(tempCancellations);

        for(MyPair pair : customer.getReservations().keySet())
            assertEquals("MyAccommodation", customer.getReservations().get(pair));

        assertTrue(customer.getCancellations().contains("MyCancellation"));
    }
    @Test
    public void printReservationTest(){
        assertEquals("> MyApartment, from 1/1/21 to 4/1/21",
                    customer.printReservation("MyApartment", 20210101, 20210104));
    }
    @Test
    public void addReservationTest(){
        Database tempDatabase = new Database("test" + File.separator + "backUpFiles" + File.separator);
        new Initializer(tempDatabase);

        Accommodation testApartment = new Apartment();
        assertTrue(customer.addReservation(testApartment, 20210101, 20210104, tempDatabase));
        assertTrue(customer.getReservations().containsValue(""));
    }
    @Test
    public void cancelReservationTest(){
        Database tempDatabase = new Database("test" + File.separator + "backUpFiles" + File.separator);
        new Initializer(tempDatabase);

        Accommodation testApartment = new Apartment();
        assertTrue(customer.addReservation(testApartment, 20210101, 20210104, tempDatabase));
        assertTrue(customer.cancelReservation(testApartment, tempDatabase));
        assertFalse(customer.getReservations().containsValue(""));
    }
}