package it.polimi.tiw.beans;

/**
 * This is a Java Bean corresponding to a object in the Database's Users table.
 * Each User has a unique ID, a username and password combo to use for login, and some personal
 * informations like name, surname and address.
 */
public class User {
    private int id;
    private String username;
    private String psw;
    private String firstName;
    private String surname;
    private String address;

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

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}