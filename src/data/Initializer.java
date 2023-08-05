package data;

import misc.Features;
import userTypes.*;
import accommodationTypes.*;
import java.io.File;
import java.util.HashSet;

/**
 * Initializer
 * This class exists so the central database is initially filled with users and accommodations
 * Also acts as a README note for future inspectors, to know login data for every user e.t.c.
 */
public class Initializer {
    /** CUSTOMERS */
    Customer customer1 = new Customer(
            "PeterPumpkinEater1337",
            "Peter",
            "Peter Griffin",
            "31 Spooner Street, Quahog, Rhode Island USA",
            "555-0112",
            true
    );
    Customer customer2 = new Customer(
            "HappyFeet76",
            "SusieKevin33",
            "Joe Swanson",
            "33 Spooner Street, Quahog, Rhode Island USA",
            "555-1234",
            true
    );
    Customer customer3 = new Customer(
            "Cleveland",
            "HeyYALL",
            "Cleveland Brown",
            "32 Spooner Street, Quahog, Rhode Island USA",
            "555-4321",
            false
    );
    Customer customer4 = new Customer(
            "Giggity_Goo",
            "p1l0t!M4n",
            "Glenn Quagmire",
            "30 Spooner Street, Quahog, Rhode Island USA",
            "555-4444",
            false
    );
    /** PROVIDERS */
    Provider provider1 = new Provider(
            "TonyMLG",
            "soCool123",
            "Antonis Apostoloudis",
            "70 Makarios Street, Thessaloniki GR",
            "6989735871",
            "101 E.Venizelos Street, Athens GR",
            true
    );
    Provider provider2 = new Provider(
            "Mitsaras420",
            "h4cK_3r$m@NN",
            "Dimitris Yfantidis",
            "30 Kolokotronis Avenue, Ioannina GR",
            "6955132460",
            "404 Silicon Valley, San Francisco, CA USA",
            true
    );
    Provider provider3 = new Provider(
            "Xalara365",
            "12345",
            "Thodoris Chronis",
            "28 Fleming Street, Thessaloniki GR",
            "6980805432",
            "28 Fleming Street, Thessaloniki GR",
            false
    );
    /** ADMINS */
    Admin admin1 = new Admin(
            "asimakis123",
            "pegadinha",
            "11 Downing Street, London UK",
            "Asimakis Kydros",
            "6989704212"
    );
    Admin admin2 = new Admin(
            "vaggelis321",
            "panserraikos",
            "5 Epirus Street, Serres GR",
            "Vaggelis Dimos",
            "6970382112"
    );
    String[] photos = {"empty"};
    /** APARTMENTS */
    Apartment apartment1 = new Apartment(
            "House by the beach",
            photos,
            """
             "House by the beach" is a wonderful resort
              for every couple or family.
              Impeccable view,
              polite and helpful staff,
              we commit to your undisturbed pleasure!""",
            new HashSet<>(),
            "27 Agias Triadas Street, Thessaloniki GR",
            100,
            5,
            provider1.getUsername()
    );
    Apartment apartment2 = new Apartment(
            "Eilot",
            photos,
            """
             "Eilot" is the escape you need this summertime!
             Loaded with all the essentials, and backed by
             spectacular view, we guarantee that you will
             have the relaxing vacation you need!""",
            new HashSet<>(),
            "15 Yemen Road, Yemen",
            70,
            3,
            provider2.getUsername()
    );
    /** MAISONETTES */
    Maisonette maisonette1 = new Maisonette(
            "Ocean Retreat",
            photos,
            """
             "Ocean Retreat", situated close to
             the breathtaking shores of the Marmara
             will, hands down, offer you the best
             experience imaginable. Close to the
             City center, expert night life.""",
            new HashSet<>(),
            "22 Kemal Street, Istanbul, TR",
            150,
            10,
            2,
            provider1.getUsername()
    );
    Maisonette maisonette2 = new Maisonette(
            "Mikro kalivi",
            photos,
            """
             Are you tired of the city life? Want
             a place to escape? Look no further than
             "Mikro Kalivi"! This impeccable maisonette
             will keep you undisturbed, and as far away
             from the busy life, as possible!""",
            new HashSet<>(),
            "Anw Partali, near Ioannina, GR",
            110,
            6,
            2,
            provider2.getUsername()
    );
    /** Hotels */
    Hotel hotel1 = new Hotel(
            "Ios Relax Hotel",
            photos,
            """
             "Ios Relax Hotel", a new neighborhood
             complex in the beautiful island of Ios,
             with its many facilities, will ensure your
             pleasure in your vacations.""",
            new HashSet<>(),
            new HashSet<>(),
            "70 Agamemnon Row, Ios GR",
            4,
            provider1.getUsername()
    );
    Hotel hotel2 = new Hotel(
            "Elpida Hotel",
            photos,
            """
             Welcome to "Elpida"! Our grand hotel
             offers the best experience in Serres.
             Whether you are staying in one of our
             impeccable rooms, or just spending the
             afternoon watching the mountain view,
             you won't leave disappointed!""",
            new HashSet<>(),
            new HashSet<>(),
            "Orini, just outside of Serres GR",
            5,
            provider2.getUsername()
    );

    public Initializer(Database dataReference){
        apartment1.addFeature(Features.WIFI);
        apartment1.addFeature(Features.ELEVATOR);
        apartment1.addFeature(Features.PARKING);
        apartment1.addFeature(Features.PROXIMITY);

        apartment2.addFeature(Features.WIFI);
        apartment2.addFeature(Features.SPA);
        apartment2.addFeature(Features.PETS);
        apartment2.addFeature(Features.PARKING);
        apartment2.addFeature(Features.SMOKING);

        maisonette1.addFeature(Features.WIFI);
        maisonette1.addFeature(Features.PROXIMITY);
        maisonette1.addFeature(Features.ELEVATOR);
        maisonette1.addFeature(Features.POOL);

        maisonette2.addFeature(Features.BAR);
        maisonette2.addFeature(Features.SMOKING);
        maisonette2.addFeature(Features.PETS);
        maisonette2.addFeature(Features.SPA);
        maisonette2.addFeature(Features.ELEVATOR);

        hotel1.addFeature(Features.WIFI);
        hotel1.addFeature(Features.ELEVATOR);
        hotel1.addFeature(Features.PARKING);
        hotel1.addFeature(Features.POOL);

        hotel2.addFeature(Features.SMOKING);
        hotel2.addFeature(Features.BAR);
        hotel2.addFeature(Features.SPA);
        hotel2.addFeature(Features.ELEVATOR);

        hotel1.addRoom(new HotelRoom(
                hotel1.getName() + ": A1",
                photos,
                "",
                new HashSet<>(),
                1,
                35,
                2,
                hotel1.getOwner()
        ));
        hotel1.addRoom(new HotelRoom(
                hotel1.getName() + ": B1",
                photos,
                "",
                new HashSet<>(),
                2,
                30,
                1,
                hotel1.getOwner()
        ));
        hotel1.addRoom(new HotelRoom(
                hotel1.getName() + ": C1",
                photos,
                "",
                new HashSet<>(),
                3,
                40,
                1,
                hotel1.getOwner()
        ));
        hotel2.addRoom(new HotelRoom(
                hotel2.getName() + ": A1",
                photos,
                "",
                new HashSet<>(),
                1,
                30,
                1,
                hotel2.getOwner()
        ));
        hotel2.addRoom(new HotelRoom(
                hotel2.getName() + ": B1",
                photos,
                "",
                new HashSet<>(),
                2,
                35,
                2,
                hotel2.getOwner()
        ));
        hotel2.addRoom(new HotelRoom(
                hotel2.getName() + ": C1",
                photos,
                "",
                new HashSet<>(),
                3,
                40,
                1,
                hotel2.getOwner()
        ));
        HotelRoom room = new HotelRoom(
                hotel2.getName() + ": D1",
                photos,
                "",
                new HashSet<>(),
                3,
                40,
                2,
                hotel2.getOwner()
        );
        hotel2.addRoom(room);

        provider1.addAccommodation(apartment1, dataReference);
        provider1.addAccommodation(maisonette1, dataReference);
        provider1.addAccommodation(hotel1, dataReference);

        provider2.addAccommodation(apartment2, dataReference);
        provider2.addAccommodation(maisonette2, dataReference);
        provider2.addAccommodation(hotel2, dataReference);

        customer1.addReservation(apartment1, 20220315, 20220320, dataReference);
        customer1.addReservation(maisonette1, 20220420, 20220423, dataReference);
        customer1.addReservation(room, 20210501, 20210510, dataReference);
        customer1.cancelReservation(room, dataReference);

        customer2.addReservation(apartment2, 20220218, 20220225, dataReference);
        customer2.addReservation(maisonette2, 20210501, 20210505, dataReference);
        customer2.cancelReservation(maisonette2, dataReference);

        admin1.sendMessage(
                "Hi peter, how are you",
                customer1,
                dataReference
        );
        admin1.sendMessage(
                "Good? Good",
                customer1,
                dataReference
        );
        admin1.sendMessage(
                "Our app is awesome",
                admin2,
                dataReference
        );
        admin1.sendMessage(
                """
                        Mister Swanson, we have examined your
                        complaint and we are sorry to inform you
                        that MyBooking has no obligation to
                        pay for your accident. We recommend just
                        walking it off. Have a nice day.
                        """,
                customer2,
                dataReference
        );
        admin2.sendMessage(
                """
                        Mister Apostoloudis, we would like
                        to inform you that "Epic Super Giga
                        Crib" is not an acceptable
                        name for an accommodation in our app.
                        Have a nice day.
                        """,
                provider1,
                dataReference
        );

        HashSet<Admin> admins = new HashSet<>();
        admins.add(admin1);
        admins.add(admin2);
        HashSet<Provider> providers = new HashSet<>();
        providers.add(provider1);
        providers.add(provider2);
        providers.add(provider3);
        HashSet<Customer> customers = new HashSet<>();
        customers.add(customer1);
        customers.add(customer2);
        customers.add(customer3);
        customers.add(customer4);
        HashSet<Accommodation> accommodations = new HashSet<>();
        accommodations.add(apartment1);
        accommodations.add(apartment2);
        accommodations.add(maisonette1);
        accommodations.add(maisonette2);
        accommodations.add(hotel1);
        accommodations.add(hotel2);

        dataReference.setRegisteredCustomers(customers);
        dataReference.setRegisteredProviders(providers);
        dataReference.setAdmins(admins);
        dataReference.setRegisteredAccommodations(accommodations);
    }

    public static void main(String[] args){
        new Initializer(new Database(
                "src" + File.separator + "data" + File.separator + "files" + File.separator
        ));
    }
}
