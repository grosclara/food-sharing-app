package com.example.cshare.data.models;

import androidx.annotation.Nullable;

import okhttp3.MultipartBody;

/**
 * Class of the User table.
 * <p>
 * The attributes defined correspond to the ones of the remote database.
 * This model is used to facilitate users' CRUD HTTP requests.
 * The defined methods are the constructor and the getters/setters.
 * @author Clara Gros
 * @author Babacar Toure
 * @version 1.0
 */
public class User {

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
    /**
     * Image url (ex: "http://127.0.0.1:8000/media/user/android.png/")
     */
    private String profile_picture;
    /**
     * Product image to upload to the server
     */
    @Nullable
    private MultipartBody.Part profilePictureBody;

    /**
     * Constructor to create a login form
     *
     * @param email
     * @param password
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Constructor to create a reset password form
     *
     * @param email
     */
    public User(String email) {
        this.email = email;
    }

    /**
     * Constructor to create a register form
     *
     * @param email
     * @param password1
     * @param password2
     * @param last_name
     * @param first_name
     * @param room_number
     * @param campus
     */
    public User(String email, String password1, String password2, String last_name, String first_name, String room_number,
                        String campus) {
        this.email = email;
        this.password1 = password1;
        this.password2 = password2;
        this.last_name = last_name;
        this.first_name = first_name;
        this.room_number = room_number;
        this.campus = campus;
    }

    /**
     * Constructor to create a reset password form
     *
     * @param oldPassword
     * @param password1
     * @param password2
     */
    public User(String oldPassword, String password1, String password2) {
        this.new_password1 = password1;
        this.new_password2 = password2;
        this.old_password = oldPassword;
    }

    // Constructor to add a new product to the VM in the add fragment

    /**
     * Constructor to create a edit profile form
     *
     * @param last_name
     * @param first_name
     * @param room_number
     * @param campus
     */
    public User(String last_name, String first_name, String room_number, String campus) {
        this.last_name = last_name;
        this.first_name = first_name;
        this.room_number = room_number;
        this.campus = campus;
    }


    public String getEmail() { return email; }

    public String getLastName() { return last_name; }

    public String getFirstName() { return first_name; }

    public String getRoomNumber() { return room_number; }

    public String getCampus() { return campus; }

    public String getProfilePictureURL() { return profile_picture; }

    public MultipartBody.Part getProfilePictureBody() { return profilePictureBody; }

    public String getOldPassword() {return old_password; }

    public String getPassword1() { return password1; }

    public String getPassword2() { return password2; }

    public int getId() { return id; }

    public void setProfilePictureBody(@Nullable MultipartBody.Part profilePictureBody) {
        this.profilePictureBody = profilePictureBody;
    }
}

