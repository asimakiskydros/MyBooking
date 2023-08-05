package accommodationTypes;

import misc.Features;
import java.io.Serializable;
import java.util.HashSet;

/**
 * Maisonette
 * A two or more floors strong house
 * Maisonettes and Apartments have many similarities, coding wise, but an inheritance relationship here
 * is error-sensitive and illogical for real life understanding
 * Inherits from Accommodation, holds extra number of floors, rooms, bedrooms, bathrooms, area and address
 */
public class Maisonette extends Accommodation implements Serializable {
    private Integer floors;
    private String address;
    private Integer area; //in square meters
    private Integer rooms;
    private Integer bedrooms;
    private Integer bathrooms;

    public Maisonette(){
        super();
        this.floors = 1;
        this.address = "";
        this.area = 0;
        this.rooms = 3;
        this.bedrooms = 1;
        this.bathrooms = 1;
    }
    public Maisonette(String name,String[] photos,String description, HashSet<Features> features,
                      String address, Integer area, Integer rooms,Integer floors, String owner){
        super(name,photos,description,features, owner);
        this.floors = floors;
        this.address = address;
        this.area = area;
        this.rooms = rooms;
        this.bedrooms = 1;
        this.bathrooms = 1;
    }
    public static final long serialVersionUID = 2L;

    //Setters
    public void setAddress(String address){
        this.address = address;
    }
    public void setArea(Integer area){
        this.area = area;
    }
    public void setRooms(Integer rooms){
        this.rooms = rooms;
    }
    public void setFloors(Integer floors){
        this.floors = floors;
    }
    /**
     * setter for bedrooms
     * checks if the number of bedrooms and bathrooms add up to the total number of rooms
     * @param bedrooms number of bedrooms the provider declares exist in the maisonette
     */
    public void setBedrooms(Integer bedrooms){
        //Number of bedrooms and bathrooms cannot exceed the number of total rooms
        if(bedrooms + this.bathrooms < this.rooms)
            this.bedrooms = bedrooms;
    }
    /**
     * setter for bathrooms
     * again does the same check as above
     * @param bathrooms number of bathrooms the provider declares exist in the maisonette
     */
    public void setBathrooms(Integer bathrooms){
        if(bathrooms + this.bedrooms < this.rooms)
            this.bathrooms = bathrooms;
    }
    //Getters
    public String getAddress(){
        return this.address;
    }
    public Integer getArea(){
        return this.area;
    }
    public Integer getRooms(){
        return this.rooms;
    }
    public Integer getBedrooms(){
        return this.bedrooms;
    }
    public Integer getBathrooms(){
        return this.bathrooms;
    }
    public Integer getFloors(){
        return this.floors;
    }
}
