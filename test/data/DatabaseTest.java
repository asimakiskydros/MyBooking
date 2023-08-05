package data;

import accommodationTypes.*;
import misc.Features;
import org.junit.Before;
import org.junit.Test;
import userTypes.*;
import java.io.File;
import java.util.HashSet;
import java.util.Objects;

import static org.junit.Assert.*;

public class DatabaseTest {

    Database tempDatabase;

    @Before
    public void setUp() throws Exception {
        tempDatabase = new Database("test" + File.separator + "backUpFiles" + File.separator);
        new Initializer(tempDatabase);
    }
    @Test
    public void PushUsersTest(){
        Customer customer = new Customer(
                "Generic Customer",
                "123",
                "Average Joe",
                "10 Average Street Name, City, Country",
                "0000000000",
                true
        );
        Provider provider = new Provider(
                "Impressive",
                "Very Nice",
                "Patrick Bateman",
                "55 West 81st Street",
                "212 555 6342",
                "358 Exchange Place, New York, NY USA",
                true
        );
        Admin admin = new Admin(
                "Mod",
                "readOurRules",
                "My basement",
                "Bob",
                "6940404040"
        );

        tempDatabase.pushRegisteredCustomer(customer);
        tempDatabase.pushRegisteredProvider(provider);
        tempDatabase.pushAdmin(admin);

        boolean customerExists = false, providerExists = false, adminExists = false;
        for(Customer user : tempDatabase.getRegisteredCustomers())
            if(Objects.equals(user.getUsername(), customer.getUsername())){
                customerExists = true;
                break;
            }
        for(Provider user : tempDatabase.getRegisteredProviders())
            if(Objects.equals(user.getUsername(), provider.getUsername())){
                providerExists = true;
                break;
            }
        for(Admin user : tempDatabase.getAdmins())
            if(Objects.equals(user.getUsername(), admin.getUsername())){
                adminExists = true;
                break;
            }

        assertTrue(customerExists);
        assertTrue(providerExists);
        assertTrue(adminExists);
    }
    @Test
    public void PushAccommodationsTest(){
        String[] photos = {"empty"};
        HashSet<Features> features = new HashSet<>();
        features.add(Features.WIFI);

        Maisonette maisonette = new Maisonette(
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
        Apartment apartment = new Apartment(
                "MyApartment",
                photos,
                "Cool Apartment",
                features,
                "11 Downing Street",
                40,
                2,
                "asimakis321"
        );
        Hotel hotel = new Hotel(
                "MyHotel",
                photos,
                "Awesome Hotel",
                features,
                new HashSet<>(),
                "11 Downing Street",
                5,
                "asimakis321"
        );
        tempDatabase.pushRegisteredAccommodation(maisonette);
        tempDatabase.pushRegisteredAccommodation(apartment);
        tempDatabase.pushRegisteredAccommodation(hotel);

        boolean maisonetteExists = false, apartmentExists = false, hotelExists = false;
        for(Accommodation accommodation : tempDatabase.getRegisteredAccommodations()){
            if(Objects.equals(accommodation.getName(), maisonette.getName()))
                maisonetteExists = true;
            if(Objects.equals(accommodation.getName(), apartment.getName()))
                apartmentExists = true;
            if(Objects.equals(accommodation.getName(), hotel.getName()))
                hotelExists = true;
        }

        assertTrue(maisonetteExists);
        assertTrue(apartmentExists);
        assertTrue(hotelExists);
    }
}