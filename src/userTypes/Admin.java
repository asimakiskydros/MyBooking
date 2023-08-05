package userTypes;

import accommodationTypes.Accommodation;
import accommodationTypes.Hotel;
import accommodationTypes.HotelRoom;
import data.Database;
import java.io.Serializable;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Objects;

/**
 * Admin
 * An admin is in charge of moderating the app, the users and their actions and listings
 * Can access the main Database directly, which is why most of their methods carry it
 * Can send messages to customers and providers
 * Inherits from User,
 * holds a map of all users, so all admins can know in constant time whether each user is admitted into the app or not
 */
public class Admin extends User implements Serializable {

    public Admin(String username, String password, String address, String name, String phoneNumber){
        super(username,password,address,name,phoneNumber,true);
    }
    public static final long serialVersionUID = 12L;
    /**
     * Search method so the admin can browse through the users
     * @param search the user's name
     * @param dataReference the central Database
     * @return the user's pointer if a registration with a username identical to the input was found
     *          null if no user has a username like that
     */
    public static User searchUsers(String search,Database dataReference){
        HashSet<Customer> tempCustomers = dataReference.getRegisteredCustomers();
        HashSet<Provider> tempProviders = dataReference.getRegisteredProviders();
        HashSet<Admin> tempAdmins = dataReference.getAdmins();

        //if not null, search for the input username in all files, fall-through style
        if(tempCustomers != null)
            for(Customer customer : tempCustomers)
                if(customer.checkUsername(search))
                    return customer;
        if(tempProviders != null)
            for(Provider provider : tempProviders)
                if(provider.checkUsername(search))
                    return provider;
        if(tempAdmins != null)
            for(Admin administrator : tempAdmins)
                if(administrator.checkUsername(search))
                    return administrator;

        //everything else failed, user does not exist or all files are empty
        return null;
    }
    /**
     * Search method to browse through the listed accommodations
     * Summons all registered accommodations from the database then checks if anyone has a name identical with the input
     * @param search the accommodation's name
     * @param dataReference the central Database
     * @return the accommodation's pointer if a listing with the given name is found
     *          null if no accommodation has a name like that
     */
    public static Accommodation searchAccommodations(String search, Database dataReference){
        HashSet<Accommodation> tempAccommodations = dataReference.getRegisteredAccommodations();
        for (Accommodation accommodation : tempAccommodations) {
            //case 1: it's an Apartment or Maisonette
            if (Objects.equals(search, accommodation.getName())) {
                return accommodation;
                //case 2: it's a room in a Hotel
            } else if (accommodation instanceof Hotel) {
                HashSet<HotelRoom> tempRooms = ((Hotel) accommodation).getRooms();
                for (HotelRoom room : tempRooms)
                    if (Objects.equals(search, room.getName()))
                        return room;
            }
        }
        //everything else failed; accommodation does not exist
        return null;
    }
    /**
     * Simple messenger method
     * Gets message in string format, then adds it to the user's message-set data and updates central database
     * @param message the string-message to be sent
     * @param user the user-receiver
     * @param dataReference the central database
     */
    public void sendMessage(String message, User user, Database dataReference){
        //update users mail database
        HashMap<String, String> temp = user.getMessages();
        temp.put(message,getName());
        user.setMessages(temp);

        //update database
        if(user instanceof Customer)
            dataReference.pushRegisteredCustomer((Customer)user);
        else if(user instanceof Provider)
            dataReference.pushRegisteredProvider((Provider)user);
        else if(user instanceof Admin)
            dataReference.pushAdmin((Admin)user);
    }
}
