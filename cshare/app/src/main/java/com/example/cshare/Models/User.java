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
    private String last_name;
    private String first_name;
    private String room_number;
    private String campus;
    private String token;
    // Correspond to the url of the picture in the server (ex: "http://127.0.0.1:8000/media/user/android.png/")
    private String profile_picture;
    private MultipartBody.Part profilePictureBody;


    public User(MultipartBody.Part profilePictureBody, String firstName, String lastName, String roomNumber, String campus, String email, String password1, String password2) {
        this.profilePictureBody = profilePictureBody;
        this.first_name = firstName;
        this.last_name = lastName;
        this.room_number = roomNumber;
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
        this.last_name = lastName;
        this.first_name = firstName;
        this.password1 = password1;
        this.password2 = password2;
        this.campus = campus;
        this.room_number = roomNumber;
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

    public User() {}

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getLastName() { return last_name; }

    public String getFirstName() {
        return first_name;
    }

    public String getRoomNumber() {
        return room_number;
    }

    public String getCampus() {
        return campus;
    }

    public String getProfilePictureURL() {
        return profile_picture;
    }

    public MultipartBody.Part getProfilePictureBody() {
        return profilePictureBody;
    }

    public String getPassword1() { return password1; }

    public String getPassword2() { return password2; }

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}

