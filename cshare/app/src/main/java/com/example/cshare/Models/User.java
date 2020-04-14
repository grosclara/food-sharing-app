package com.example.cshare.Models;

import java.io.Serializable;

import okhttp3.MultipartBody;

/**
 * Class of the User table.
 * The attributes defined corresponds to the ones of the remote database.
 * This model is used to facilitate user's CRUD HTTP requests.
 * The defined methods are the constructor and the getters/setters.
 *
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class User implements Serializable {

    private int id;
    private String email;
    private String password;
    private String password1;
    private String password2;
    private String new_password1;
    private String new_password2;
    private String old_password;
    private String lastName;
    private String firstName;
    private String roomNumber;
    private String campus;
    private String token;
    // Correspond to the url of the picture in the server (ex: "http://127.0.0.1:8000/media/user/android.png/")
    private String profilePictureURL;
    private MultipartBody.Part profilePictureBody;


    public User(MultipartBody.Part profilePictureBody, String firstName, String lastName, String roomNumber, String campus, String email, String password1, String password2) {
        this.profilePictureBody = profilePictureBody;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roomNumber = roomNumber;
        this.campus = campus;
        this.email = email;
        this.password1 = password1;
        this.password2 = password2;
    }

    /*
     * Constructor of the User class.
     * Only take a few attributes in argument because the server auto adds the others (id, created_at, updated_at)
     */
    public User(String email, String lastName, String firstName, String password1, String password2, String campus, String roomNumber) {
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.password1 = password1;
        this.password2 = password2;
        this.campus = campus;
        this.roomNumber = roomNumber;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String oldPassword, String password1, String password2) {
        this.new_password1 = password1;
        this.new_password2 = password2;
        this.old_password = oldPassword;
    }

    public User(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getCampus() {
        return campus;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public MultipartBody.Part getProfilePictureBody() {
        return profilePictureBody;
    }

    public String getPassword1() {
        return password1;
    }

    public String getPassword2() {
        return password2;
    }
}

