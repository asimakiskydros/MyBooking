package userTypes;

import java.util.*;
import accommodationTypes.*;
import data.Database;
import misc.MyPair;

import java.io.Serializable;

/**
 * Provider
 * Providers own accommodations and can choose to upload them to the app so customers can book them
 * They can also take them down or edit their features and characteristics
 * Can receive messages from Admins
 * Inherits from User, holds address of main headquarters and set of all owned (and registered) accommodations
 */
public class Provider extends User implements Serializable{
    private String addressHQ;
    private HashSet<String> ownedAccommodations;

    public Provider(String username, String password, String name,
                    String address, String phoneNumber,String addressHQ, boolean admitted){
        super(username,password,address,name,phoneNumber,admitted);
        this.addressHQ = addressHQ;
        this.ownedAccommodations = new HashSet<>();
    }
    public static final long serialVersionUID = 52L;

    //Setters
    public void setAddressHQ(String addressHQ){
        this.addressHQ = addressHQ;
    }
    public void setOwnedAccommodations(HashSet<String> ownedAccommodations){
        this.ownedAccommodations = ownedAccommodations;
    }
    //Getters
    public String getAddressHQ(){
        return this.addressHQ;
    }
    public HashSet<String> getOwnedAccommodations(){
        return this.ownedAccommodations;
    }
    /**
     * Each provider has a reservations score, that highlights how many times a listing they own (any listing)
     * has been reserved
     * @param dataReference the central database
     * @return the sum of the times each owned accommodation has been booked
     */
    public Integer getTotalReservations(Database dataReference){
        HashSet<Accommodation> temp = dataReference.getRegisteredAccommodations();
        if(temp == null) return 0;
        int sum=0;
        for(Accommodation listing : temp) {
            if(this.ownedAccommodations.contains(listing.getName()))
                sum += listing.getTimesReserved();
        }
        return sum;
    }
    /**
     * Similarly, providers have cancellation scores, that highlight how many times any one of their listings
     * has been annulled from a booking
     * @param dataReference the central database
     * @return the sum of the times each owned accommodation has been cancelled
     */
    public Integer getTotalCancellations(Database dataReference){
        HashSet<Accommodation> temp = dataReference.getRegisteredAccommodations();
        if(temp == null) return 0;
        int sum=0;
        for(Accommodation listing : temp)
            if(this.ownedAccommodations.contains(listing.getName()))
                sum+=listing.getTimesCancelled();
        return sum;
    }
    /**
     * adder-updater of owned accommodations set
     * @param accommodation a new owned listing
     * @param dataReference the central database
     * @return true if the listing was added to the data-set successfully
     */
    public Boolean addAccommodation(Accommodation accommodation, Database dataReference){
        boolean ret =  this.ownedAccommodations.add(accommodation.getName());
        if(ret) {
            //update databases
            dataReference.pushRegisteredProvider(this);
            dataReference.pushRegisteredAccommodation(accommodation);
        }
        return ret;
    }
    /**
     * Method to take down accommodations from database
     * if found, attempts to delete it
     * @param accommodation the listing to be deleted
     * @param dataReference the central database
     * @return true if accommodation was found and successfully removed from data-set
     */
    @SuppressWarnings({"UnusedReturnValue", "StringConcatenationInLoop"})
    public Boolean removeAccommodation(Accommodation accommodation, Database dataReference){
        if(accommodation instanceof HotelRoom){
            //hotelRooms have their bound hotel's name inside their own name
            String hotelName = "";
            char[] roomName = accommodation.getName().toCharArray();
            //copy hotel name
            for(char c : roomName){
                if(c == ':') break;
                hotelName += c;
            }
            //fetch hotel object and attempt room removal
            Accommodation boundHotel = Admin.searchAccommodations(hotelName, dataReference);
            if(boundHotel != null && Objects.equals(boundHotel.getOwner(), this.getUsername())){
                boolean ret = ((Hotel) boundHotel).removeRoom((HotelRoom) accommodation);
                if(ret) {
                    dataReference.pushRegisteredHotel((Hotel) boundHotel);
                    removeLeftoverReservationsOf(accommodation, dataReference);
                }
                return ret;
            }
            else return false;
        }
        //Case: anything but a hotelRoom
        //wrong parameter safeguard
        boolean ret = this.ownedAccommodations.contains(accommodation.getName());
        HashSet<Accommodation> temp = dataReference.getRegisteredAccommodations();
        boolean success = (temp != null &&
                temp.removeIf(accommodation1 -> Objects.equals(accommodation1.getName(), accommodation.getName())));

        if (ret && success) {
            //accommodation existed, wipe it from database
            this.ownedAccommodations.remove(accommodation.getName());
            removeLeftoverReservationsOf(accommodation, dataReference);
            dataReference.setRegisteredAccommodations(temp);
            dataReference.pushRegisteredProvider(this);
        }
        return ret && success;
    }
    private void removeLeftoverReservationsOf(Accommodation cancellation, Database dataReference){
        HashSet<Customer> tempCustomers = dataReference.getRegisteredCustomers();
        if(tempCustomers != null)
            for(Customer person : tempCustomers){
                HashMap<MyPair, String> tempReservations = person.getReservations();
                for(MyPair dates : tempReservations.keySet())
                    if(Objects.equals(tempReservations.get(dates), cancellation.getName()))
                        tempReservations.remove(dates);
            }
        dataReference.setRegisteredCustomers(tempCustomers);
    }

}
