package data;

import userTypes.*;
import accommodationTypes.*;
import java.util.*;
import java.io.*;

/**
 * Database
 * Main frame of reference for every call or store function in terms of data
 * Reads from files when called, holds data in memory while needed, then updates and gets destroyed
 * Holds sets for the registered providers and customers, and a map for all users
 */

class MyObjectOutputStream extends ObjectOutputStream{
    public MyObjectOutputStream(OutputStream o) throws IOException{
        super(o);
    }
    @Override
    protected void writeStreamHeader() {
    }
}
class MyObjectInputStream extends ObjectInputStream{
    public MyObjectInputStream(InputStream i) throws  IOException{
        super(i);
    }
    @Override
    protected void readStreamHeader(){
    }
}

public class Database implements Serializable{
    String  filenameProviders,
            filenameCustomers,
            filenameAdmins,
            filenameApartments,
            filenameMaisonettes,
            filenameHotels,
            directory;

    public Database(String folderDestination){
        this.filenameCustomers = "Customers.dat";
        this.filenameProviders = "Providers.dat";
        this.filenameAdmins = "Admins.dat";
        this.filenameApartments = "Apartments.dat";
        this.filenameMaisonettes = "Maisonettes.dat";
        this.filenameHotels = "Hotels.dat";
        this.directory = System.getProperty("user.dir") + File.separator + folderDestination;
    }
    public static final long serialVersionUID = 42L;

    //Setters
    /**
     * Single provider adder-updater
     * Updates provider file by adding new person/updating existing
     * @param person the provider to be added
     */
    public void pushRegisteredProvider(Provider person){
        //First, check if the Provider that we attempt to push already exists
        //by looking for its username (immutable)
        HashSet<Provider> tempProviders = getRegisteredProviders();
        if(tempProviders != null)
            tempProviders.removeIf(provider -> Objects.equals(provider.getUsername(), person.getUsername()));
        else
            tempProviders = new HashSet<>();

        //add person
        tempProviders.add(person);
        //update database
        setRegisteredProviders(tempProviders);
    }
    /**
     * Single customer adder-updater
     * Updates the customer file by adding new person/updating existing
     * @param person the customer to be added
     */
    public void pushRegisteredCustomer(Customer person){
        //Similarly, check if this customer already exists
        HashSet<Customer> tempCustomers = getRegisteredCustomers();
        if(tempCustomers != null)
            tempCustomers.removeIf(customer -> Objects.equals(customer.getUsername(), person.getUsername()));
        else
            tempCustomers = new HashSet<>();

        //add person
        tempCustomers.add(person);
        //update database
        setRegisteredCustomers(tempCustomers);
    }
    /**
     * Single admin adder-updater
     * Updates the admin file by adding new administrator/updating existing
     * @param administrator the admin to be added
     */
    public void pushAdmin(Admin administrator){
        //Once again, check if admin already exists first
        HashSet<Admin> tempAdmins = getAdmins();
        if(tempAdmins != null)
            tempAdmins.removeIf(admin -> Objects.equals(admin.getUsername(), administrator.getUsername()));
        else
            tempAdmins = new HashSet<>();

        //add admin
        tempAdmins.add(administrator);
        //update database
        setAdmins(tempAdmins);
    }
    /**
     * Single accommodation adder-updater
     * Determines the type of the listing that is to be pushed, then activates the correct function
     * @param listing the accommodation to be added
     */
    public void pushRegisteredAccommodation(Accommodation listing){
        if(listing instanceof Apartment)
            pushRegisteredApartment((Apartment) listing);
        else if(listing instanceof Maisonette)
            pushRegisteredMaisonette((Maisonette) listing);
        else if(listing instanceof Hotel)
            pushRegisteredHotel((Hotel) listing);
    }
    public void pushRegisteredApartment(Apartment listing){
        //Firstly, check if the apartment we attempt to push already exists
        //by looking for its name (should be unique for each accommodation)
        HashSet<Apartment> tempApartments = getRegisteredApartments();
        if(tempApartments != null)
            tempApartments.removeIf(apartment -> Objects.equals(apartment.getName(), listing.getName()));
        else
            tempApartments = new HashSet<>();

        //add listing
        tempApartments.add(listing);
        //update database
        setRegisteredApartments(tempApartments);
    }
    public void pushRegisteredMaisonette(Maisonette listing){
        //Firstly, check if the apartment we attempt to push already exists
        //by looking for its name (should be unique for each accommodation)
        HashSet<Maisonette> tempMaisonettes = getRegisteredMaisonettes();
        if(tempMaisonettes != null)
            tempMaisonettes.removeIf(maisonette -> Objects.equals(maisonette.getName(), listing.getName()));
        else
            tempMaisonettes = new HashSet<>();

        //add listing
        tempMaisonettes.add(listing);
        //update database
        setRegisteredMaisonettes(tempMaisonettes);
    }
    public void pushRegisteredHotel(Hotel listing){
        //Firstly, check if the apartment we attempt to push already exists
        //by looking for its name (should be unique for each accommodation)
        HashSet<Hotel> tempHotels = getRegisteredHotels();
        if(tempHotels != null)
            tempHotels.removeIf(hotel -> Objects.equals(hotel.getName(), listing.getName()));
        else
            tempHotels = new HashSet<>();

        //add listing
        tempHotels.add(listing);
        //update database
        setRegisteredHotels(tempHotels);
    }
    /**
     * Multiple provider adder
     * Overrides the providers file with the parameter data
     * @param registeredProviders the new set of providers
     */
    public void setRegisteredProviders(HashSet<Provider> registeredProviders){
        //FORBID NULL INPUT
        if(registeredProviders == null) return;

        String filename = this.directory + this.filenameProviders;
        //Push each person saved in the hashset to the file individually
        try(MyObjectOutputStream oos = new MyObjectOutputStream(new FileOutputStream(filename))){
            for(Provider person : registeredProviders)
                oos.writeObject(person);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    /**
     * Multiple customer adder
     * Overrides the customers file with the parameter data
     * @param registeredCustomers the new set of customers
     */
    public void setRegisteredCustomers(HashSet<Customer> registeredCustomers){
        //FORBID NULL INPUT
        if(registeredCustomers == null) return;

        String filename = this.directory + this.filenameCustomers;
        //Push each person saved in the hashset to the file individually
        try(MyObjectOutputStream oos = new MyObjectOutputStream(new FileOutputStream(filename))){
            for(Customer person : registeredCustomers)
                oos.writeObject(person);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    /**
     * Multiple Accommodations setter
     * Filters the accommodation set into smaller, specific type-sets then pushes them to the database
     * to override their corresponding files
     * @param accommodations the new set of accommodations
     */
    public void setRegisteredAccommodations(HashSet<Accommodation> accommodations){
        //FORBID NULL INPUT
        if(accommodations == null) return;

        HashSet<Apartment> tempApartments = new HashSet<>();
        HashSet<Maisonette> tempMaisonettes = new HashSet<>();
        HashSet<Hotel> tempHotels = new HashSet<>();
        //dissect accommodations set
        for(Accommodation listing : accommodations){
            if(listing instanceof Apartment)
                tempApartments.add((Apartment) listing);
            else if(listing instanceof Maisonette)
                tempMaisonettes.add((Maisonette) listing);
            else if(listing instanceof Hotel)
                tempHotels.add((Hotel) listing);
        }
        //push each subset individually
        setRegisteredApartments(tempApartments);
        setRegisteredMaisonettes(tempMaisonettes);
        setRegisteredHotels(tempHotels);
    }
    /**
     * Multiple Apartments setter
     * Overrides the apartments file with the new data
     * @param apartments the new set of apartments
     */
    public void setRegisteredApartments(HashSet<Apartment> apartments){
        //FORBID NULL INPUT
        if(apartments == null) return;

        String filename = this.directory + this.filenameApartments;
        //Push each listing to the file individually
        try(MyObjectOutputStream oos = new MyObjectOutputStream(new FileOutputStream(filename))){
            for(Apartment listing : apartments)
                oos.writeObject(listing);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    /**
     * Multiple Maisonettes setter
     * Overrides the maisonettes file with the new data
     * @param maisonettes the new set of maisonettes
     */
    public void setRegisteredMaisonettes(HashSet<Maisonette> maisonettes){
        //FORBID NULL INPUT
        if(maisonettes == null) return;

        String filename = this.directory + this.filenameMaisonettes;
        //Push each listing to the file individually
        try(MyObjectOutputStream oos = new MyObjectOutputStream(new FileOutputStream(filename))){
            for(Maisonette listing : maisonettes)
                oos.writeObject(listing);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    /**
     * Multiple Hotels setter
     * Overrides the hotels file with the new data
     * @param hotels the new set of hotels
     */
    public void setRegisteredHotels(HashSet<Hotel> hotels){
        //FORBID NULL INPUT
        if(hotels == null) return;

        String filename = this.directory + this.filenameHotels;
        //Push each listing to the file individually
        try(MyObjectOutputStream oos = new MyObjectOutputStream(new FileOutputStream(filename))){
            for(Hotel listing : hotels)
                oos.writeObject(listing);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    /**
     * Multiple Admins setter
     * Overrides the admins file with the parameter data
     * @param admins the new set of admins
     */
    public void setAdmins(HashSet<Admin> admins){
        //FORBID NULL INPUT
        if(admins == null) return;

        String filename = this.directory + this.filenameAdmins;
        //Push each listing to the file individually
        try(MyObjectOutputStream oos = new MyObjectOutputStream(new FileOutputStream(filename))){
            for(Admin administrator : admins)
                oos.writeObject(administrator);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    //Getters
    /**
     * Entire provider file getter
     * fetches all saved providers and saves them in a temporary set
     * @return the temporary hashset of providers
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public HashSet<Provider> getRegisteredProviders(){
        String filename = this.directory + this.filenameProviders;
        HashSet<Provider> registeredProviders = new HashSet<>();
        try(MyObjectInputStream ois = new MyObjectInputStream(new FileInputStream(filename))){
            //read all contents of file and save them in the temporary set
            while(true)
                registeredProviders.add((Provider)ois.readObject());
        }catch(FileNotFoundException e){
            //File doesn't exist
            return null;
        }catch(EOFException e){
            //EOF reached
            return registeredProviders;
        }catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Entire customer file getter
     * fetches all saved customers and saves them in a temporary set
     * @return the temporary hashset of customers
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public HashSet<Customer> getRegisteredCustomers(){
        String filename = this.directory + this.filenameCustomers;
        HashSet<Customer> registeredCustomers = new HashSet<>();
        try(MyObjectInputStream ois = new MyObjectInputStream(new FileInputStream(filename))){
            //read all contents of file and save them in the temporary set
            while(true)
                registeredCustomers.add((Customer)ois.readObject());
        }catch(FileNotFoundException e){
            //File doesn't exist
            return null;
        }catch(EOFException e){
            //EOF reached
            return registeredCustomers;
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Entire admin file getter
     * fetches all saved admins and saves them in temporary set
     * @return the temporary hashset of admins
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public HashSet<Admin> getAdmins(){
        String filename = this.directory + this.filenameAdmins;
        HashSet<Admin> admins = new HashSet<>();
        try(MyObjectInputStream ois = new MyObjectInputStream(new FileInputStream(filename))){
            //read all contents of file and save them in temporary set
            while(true)
                admins.add((Admin)ois.readObject());
        }catch(FileNotFoundException e){
            //File doesn't exist
            return null;
        }catch(EOFException e){
            //EOF reached
            return admins;
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Getter for all accommodation types
     * Requests all different accommodation types and fuses them into a single set
     * @return a set of all accommodations
     */
    public HashSet<Accommodation> getRegisteredAccommodations(){
        HashSet<Accommodation> registeredAccommodations = new HashSet<>();
        HashSet<Apartment> tempApartments = getRegisteredApartments();
        HashSet<Maisonette> tempMaisonettes = getRegisteredMaisonettes();
        HashSet<Hotel> tempHotels = getRegisteredHotels();

        if(tempApartments != null)
            registeredAccommodations.addAll(tempApartments);
        if(tempMaisonettes != null)
            registeredAccommodations.addAll(tempMaisonettes);
        if(tempHotels != null)
            registeredAccommodations.addAll(tempHotels);

        return registeredAccommodations;
    }
    /**
     * Apartments getter
     * Scans the apartments file and returns a set of all registrations
     * @return a set of all saved apartments
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public HashSet<Apartment> getRegisteredApartments(){
        String filename = this.directory + this.filenameApartments;
        HashSet<Apartment> registeredApartments = new HashSet<>();
        try(MyObjectInputStream ois = new MyObjectInputStream(new FileInputStream(filename))){
            while(true)
                registeredApartments.add((Apartment)ois.readObject());
        }catch(FileNotFoundException e){
            //File does not exist
            return null;
        }catch(EOFException e){
            //EOF reached
            return registeredApartments;
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Maisonettes getter
     * Scans the maisonettes file and returns a set of all registrations
     * @return a set of all saved maisonettes
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public HashSet<Maisonette> getRegisteredMaisonettes(){
        String filename = this.directory + this.filenameMaisonettes;
        HashSet<Maisonette> registeredMaisonettes = new HashSet<>();
        try(MyObjectInputStream ois = new MyObjectInputStream(new FileInputStream(filename))){
            while(true)
                registeredMaisonettes.add((Maisonette) ois.readObject());
        }catch(FileNotFoundException e){
            //File does not exist
            return null;
        }catch(EOFException e){
            //EOF reached
            return registeredMaisonettes;
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Hotels getter
     * Scans the hotels file and returns a set of all registrations
     * @return a set of all saved hotels
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public HashSet<Hotel> getRegisteredHotels(){
        String filename = this.directory + this.filenameHotels;
        HashSet<Hotel> registeredHotels = new HashSet<>();
        try(MyObjectInputStream ois = new MyObjectInputStream(new FileInputStream(filename))){
            while(true)
                registeredHotels.add((Hotel) ois.readObject());
        }catch(FileNotFoundException e){
            //File does not exist
            return null;
        }catch(EOFException e){
            //EOF reached
            return registeredHotels;
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args){
        new Database("src" + File.separator +  "data" + File.separator + "files" + File.separator).
                pushRegisteredCustomer(
                    new Customer(
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    true
                )
            );
    }
}
