package userTypes;

import java.util.*;
import java.io.Serializable;

/**
 * Master class User
 * Customers, Providers and Admins all carry some generic, similar characteristics and can at times be thought as
 * (and managed as, coding wise) a unified concept
 * Holds basic characteristics, such as username, password, full name, address and phone number
 */
public class User implements Serializable{
    final private String username;
    final private String password;
    private String name;
    private String phoneNumber;
    private String address;
    private boolean admitted;
    //String 1: message String 2: author
    private HashMap<String,String> messages;

    public User(String username, String password, String address, String name, String phoneNumber, boolean admitted){
        this.username = username;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.admitted = admitted;
        this.messages = new HashMap<>();
    }
    public static final long serialVersionUID = 32L;
    /**
     * username checker
     * @param username input from user
     * @return true if the user input is correct, ie matches the registered username
     */
    public Boolean checkUsername(String username){
        return Objects.equals(this.username, username);
    }
    /**
     * password checker
     * @param password input form user
     * @return true if the user input is correct, ie matches the registered password
     */
    public Boolean checkPassword(String password){
        return Objects.equals(this.password, password);
    }
    //Getters
    public String getUsername(){
        return this.username;
    }
    public String getName(){
        return this.name;
    }
    public String getPhoneNumber(){
        return this.phoneNumber;
    }
    public String getAddress(){
        return this.address;
    }
    public HashMap<String, String> getMessages() {
        return this.messages;
    }
    public boolean getAdmitted(){
        return this.admitted;
    }
    //Setters
    public void setName(String name){
        this.name = name;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public void setMessages(HashMap<String,String> messages){
        this.messages = messages;
    }
    public void setAdmitted(boolean admitted){
        this.admitted = admitted;
    }
}
