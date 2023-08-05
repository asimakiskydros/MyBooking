package accommodationTypes;

import misc.Features;
import java.io.Serializable;
import java.util.HashSet;

/**
 * Apartment
 * A single floor, usually two or more room strong house
 * Inherits from accommodation, holds extra address, area, number of rooms, bedrooms and bathrooms
 */
public class Apartment extends Accommodation implements Serializable {
    private String address;
    private Integer area; //in square meters
    private Integer rooms;
    private Integer bedrooms;
    private Integer bathrooms;

    public Apartment(){
        super();
        this.address = "";
        this.area = 0;
        this.rooms = 3;
        this.bedrooms = 1;
        this.bathrooms = 1;
    }
    public Apartment(String name, String[] photos, String description,
                     HashSet<Features> features, String address, Integer area, Integer rooms, String owner){
        super(name,photos,description,features,owner);
        this.address = address;
        this.area = area;
        this.rooms = rooms;
        this.bedrooms = 1;
        this.bathrooms = 1;
    }
    public static final long serialVersionUID = 72L;
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
    /**
     * setter for bedrooms
     * checks if the number inserted + the existing bathrooms add up to the total number of rooms
     * @param bedrooms number of bedrooms the provider declares exist in the apartment
     */
    public void setBedrooms(Integer bedrooms){
        //Number of bedrooms and bathrooms cannot exceed the number of total rooms
        if(bedrooms + this.bathrooms < this.rooms)
            this.bedrooms = bedrooms;
    }
    /**
     * setter for bathrooms
     * again checks if the amount of bedrooms and bathrooms add up to the total
     * @param bathrooms number of bathrooms the provider declares exist in the apartment
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
}

