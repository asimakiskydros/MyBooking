package accommodationTypes;

import misc.Features;
import java.io.Serializable;
import java.util.*;

/**
 * Master class Accommodation
 * Every listing a provider can upload and manage and a customer book is an accommodation
 * Apartments, Hotels, Hotel Rooms and Maisonettes can all be interpreted as a single thing
 * Holds generic info, such as a name, a description, features and photos
 */
public class Accommodation implements Serializable {
    private String name;
    private String[] photos;
    private String description;
    private HashSet<Features> features;
    private Integer timesReserved;
    private Integer timesCancelled;
    private final HashMap<Integer,Integer> checkInCheckOuts;
    private String owner;

    public Accommodation(){
        this.name = "";
        this.photos = null;
        this.description = "";
        this.features = new HashSet<>();
        this.timesCancelled = 0;
        this.timesReserved = 0;
        this.checkInCheckOuts = new HashMap<>();
    }
    public Accommodation(String name,String[] photos,String description, HashSet<Features> features, String owner){
        this.name = name;
        this.photos = photos;
        this.description = description;
        this.features = features;
        this.timesCancelled = 0;
        this.timesReserved = 0;
        this.checkInCheckOuts = new HashMap<>();
        this.owner = owner;
    }
    public static final long serialVersionUID = 62L;
    //Setters
    public void setName(String name){
        this.name = name;
    }
    public void setPhotos(String[] photos){
        this.photos = photos;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setFeatures(HashSet<Features> features){
        this.features = features;
    }
    public void addFeature(Features feature){
        this.features.add(feature);
    }
    //Getters
    public String getName(){
        return this.name;
    }
    public String[] getPhotos(){
        return this.photos;
    }
    public String getDescription(){
        return this.description;
    }
    public HashSet<Features>getFeatures(){
        return this.features;
    }
    public Integer getTimesReserved(){
        return this.timesReserved;
    }
    public Integer getTimesCancelled(){
        return this.timesCancelled;
    }
    public String getOwner(){
        return this.owner;
    }
    /**
     * Method to add a reservation
     * @param checkIn date of check-in, coded into an integer through the DateSystem class
     * @param checkOut date of check-out, again coded into an integer
     * @return true if checkIn<checkOut (only way it makes sense) and the reservation was successfully added to the set
     */
    public Boolean addReservation(int checkIn, int checkOut){
        //update score
        this.timesReserved++;
        for(int registeredCheckIn : this.checkInCheckOuts.keySet()){
            //cannot have multiple reservations in the same time frame
            //thus [checkIn,checkOut] needs to not be contained within [registeredCheckIn, registeredCheckOut]
            //as it is assumed that checkIn<CheckOut, return true if checkIn>=registeredCheckOut AND checkOut<=registeredCheckIn
            //( = as different people can check in and check out from the same room the same day)
            if(checkIn<this.checkInCheckOuts.get(registeredCheckIn) || checkOut>registeredCheckIn)
                return false;
        }
        this.checkInCheckOuts.put(checkIn,checkOut);
        return true;
    }
    /**
     * Method to remove a reservation
     * @param checkIn date of check-in, coded into an integer
     * @param checkOut date of check-out, coded into an integer
     * @return true if the reservation already existed and got successfully removed
     */
    public Boolean removeReservation(int checkIn, int checkOut){
        boolean ret = this.checkInCheckOuts.remove(checkIn,checkOut);
        if(ret) {
            //update scores only if the reservation was found and cancelled
            this.timesReserved--;
            this.timesCancelled++;
        }
        return ret;
    }
}
