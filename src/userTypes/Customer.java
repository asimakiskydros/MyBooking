package userTypes;

import java.util.*;
import accommodationTypes.Accommodation;
import java.io.Serializable;
import data.Database;
import misc.MyPair;

/**
 * Customer
 * The main user type. Customers can see all the registered destinations and plan their vacations
 * Can book but also cancel bookings
 * Can receive and read messages from admins
 * Inherits from User, holds map of all reservations with their dates and set of all cancellations
 */
public class Customer extends User implements Serializable{
    //The "reservations" hashmap should hold the reserved house and the check in and check out dates pair
    private HashMap<MyPair, String> reservations;
    private HashSet<String> cancellations;

    public Customer(String username, String password,  String name,
                    String address, String phoneNumber, boolean admitted){
        super(username,password,address,name,phoneNumber,admitted);
        this.reservations = new HashMap<>();
        this.cancellations = new HashSet<>();
    }
    public static final long serialVersionUID = 22L;
    /**
     * Push reservation to map method
     * @param reservation the pointer of the accommodation the customer wishes to book
     * @param checkIn the check-in date coded into an integer
     * @param checkOut the check-out date coded into an integer
     * @param dataReference the central database
     * @return true if the reservation was successfully created and pushed
     */
    public Boolean addReservation(Accommodation reservation, int checkIn, int checkOut, Database dataReference){
        boolean ret = reservation.addReservation(checkIn,checkOut);
        //if the reservation was successful, only then add it into the database
        if(ret) {
            this.reservations.put(new MyPair(checkIn, checkOut), reservation.getName());
            dataReference.pushRegisteredCustomer(this);
            dataReference.pushRegisteredAccommodation(reservation);
        }
        return ret;
    }
    /**
     * Cancel reservation method
     * Asks for name of reservation, goes through customer's database, and if the reservation exists, removes it from
     * reservations map and adds it on cancellations set, so it is projectable in customer's profile
     * @param dataReference the central database
     * @return true if reservation was found and successfully removed from reservation map and added to cancellation
     * set. False if typo in name or reservation never existed to begin with.
     */
    public Boolean cancelReservation(Accommodation reservation, Database dataReference){
        boolean ret = this.reservations.containsValue(reservation.getName());
        if(ret){
            //reservation exists, wipe booking info from customer and accommodation database
            MyPair tempDates = new MyPair(0,0);
            for(MyPair pair : this.reservations.keySet())
                if(Objects.equals(this.reservations.get(pair), reservation.getName())){
                    tempDates = pair;
                    break;
                }
            this.reservations.remove(tempDates);
            this.cancellations.add(reservation.getName());
            reservation.removeReservation(tempDates.getHead(), tempDates.getValue());

            //update customer and accommodation files
            dataReference.pushRegisteredCustomer(this);
            dataReference.pushRegisteredAccommodation(reservation);
        }
        return ret;
    }
    //Setters
    public void setReservations(HashMap<MyPair, String> reservations) {
        //FORBID NULL INPUT
        //makes no sense + it messes everything up
        if(reservations == null) return;
        this.reservations = reservations;
    }
    public void setCancellations(HashSet<String> cancellations) {
        //FORBID NULL INPUT
        //same thing
        if(cancellations == null) return;
        this.cancellations = cancellations;
    }
    //Getters
    public HashMap<MyPair, String> getReservations(){
        return this.reservations;
    }
    public HashSet<String> getCancellations(){
        return this.cancellations;
    }
    /**
     * Active reservation with dates printer
     * prints reservation's name and check-in and check-out dates
     * @param reservation the active reservation's name in question
     */
    public String printReservation(String reservation, int checkIn, int checkOut){
        //format reservation into something like "> Paros resort, from 11/11/21 to 13/11/21"
        String ret = "> " + reservation + ", from ";

        //check-in date
        ret +=  checkIn%100 + "/";
        checkIn/=100;
        ret += checkIn%100 + "/";
        checkIn/=100;
        ret += checkIn%100 + " to ";

        //check-out date
        ret += checkOut%100 + "/";
        checkOut/=100;
        ret += checkOut%100 + "/";
        checkOut/=100;
        ret += checkOut%100 + "";

        return ret;
    }
}
