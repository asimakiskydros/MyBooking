package accommodationTypes;

import misc.Features;
import java.io.Serializable;
import java.util.HashSet;

/**
 * HotelRoom
 * A provider lists a hotel, but does not plan to sell the hotel. He sells the rooms.
 * Thus, a Hotel, be it an accommodation, holds accommodations.
 * Inherits from Accommodation, holds extra number of beds,which floor it's in and area
 */
public class HotelRoom extends Accommodation implements Serializable {
    private Integer floor;
    private Integer area; // in square meters
    private Integer beds;

    public HotelRoom(String name,String[] photos,String description, HashSet<Features> features,
                     Integer floor,Integer area,Integer beds, String owner){
        super(name,photos,description,features, owner);
        this.floor = floor;
        this.area = area;
        this.beds = beds;
    }
    public static final long serialVersionUID = 92L;

    //Setters
    public void setFloor(Integer floor){
        this.floor = floor;
    }
    public void setBeds(Integer beds){
        this.beds = beds;
    }
    public void setArea(Integer area){
        this.area = area;
    }
    //Getters
    public Integer getFloor(){
        return this.floor;
    }
    public Integer getArea(){
        return this.area;
    }
    public Integer getBeds(){
        return this.beds;
    }
}
