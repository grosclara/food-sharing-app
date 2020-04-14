package com.example.cshare.Models;

import okhttp3.MultipartBody;

public class UserWithPicture {

    // Constructor of a created user that fit the post HTTP required format

    private MultipartBody.Part profilePicture;
    private String firstName;
    private String lastName;
    private String room_number;
    private String campus;
    private String email;
    private String password1;
    private String password2;

    public UserWithPicture(MultipartBody.Part profilePicture, String firstName, String lastName, String room_number, String campus, String email, String password1, String password2) {
        this.profilePicture = profilePicture;
        this.firstName = firstName;
        this.lastName = lastName;
        this.room_number = room_number;
        this.campus = campus;
        this.email = email;
        this.password1 = password1;
        this.password2 = password2;
    }

    public MultipartBody.Part getProfilePicture() {
        return profilePicture;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRoom_number() {
        return room_number;
    }

    public String getCampus() {
        return campus;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword1() {
        return password1;
    }

    public String getPassword2() {
        return password2;
    }
}
