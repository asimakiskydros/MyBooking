package accommodationTypes;

import misc.Features;
import java.io.Serializable;
import java.util.*;

/**
 * Hotel
 * An accommodation of accommodations (Hotel Rooms)
 * Could have been a standalone type of listing (ie not an accommodation) but this simplifies management
 * Inherits from Accommodation, holds extra rooms, address, number of floors
 */
public class Hotel extends Accommodation implements Serializable {
    private HashSet<HotelRoom> rooms;
    private String address;
    private Integer floors;

    public Hotel(){
        super();
        this.rooms = new HashSet<>();
        this.address = "";
        this.floors = 0;
    }
    public Hotel(String name,String[] photos,String description, HashSet<Features> features,
                 HashSet<HotelRoom> rooms,String address,Integer floors,String owner){
        super(name,photos,description,features, owner);
        this.rooms = rooms;
        this.address = address;
        this.floors = floors;
    }
    public static final long serialVersionUID = 82L;

    public Boolean addRoom(HotelRoom room){
        return this.rooms.add(room);
    }
    public Boolean removeRoom(HotelRoom room){
        return this.rooms.removeIf(hotelRoom -> Objects.equals(hotelRoom.getName(), room.getName()));
    }
    //Setters
    public void setRooms(HashSet<HotelRoom> rooms){
        this.rooms = rooms;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public void setFloors(Integer floors){
        this.floors = floors;
    }
    //Getters
    public HashSet<HotelRoom> getRooms(){
        return this.rooms;
    }
    public String getAddress(){
        return this.address;
    }
    public Integer getFloors() {
        return floors;
    }
}
