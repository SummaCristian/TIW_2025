package it.polimi.tiw.beans;

/**
 * This is a Java Bean corresponding to a object in the Database's Users table.
 * Each User has a unique ID, a username and password combo to use for login, and some personal
 * informations like name, surname and address.
 */
public class User {
    private int id;
    private String username;
    // NO PASSWORD FIELD
    private String firstName;
    private String lastName;
    private String address;
    
    // --- Constructor ---
    
    // Empty Constructor
    public User() {}
    
    // Full Constructor
    public User(
    	int id,
    	String username,
    	String firstName,
    	String lastName,
    	String address
    ) {
    	this.id = id;
    	this.username = username;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.address = address;
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return lastName;
    }

    public void setSurname(String surname) {
        this.lastName = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}