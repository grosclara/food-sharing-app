package com.example.cshare.Models.Auth;

import androidx.annotation.Nullable;

import java.io.File;

import okhttp3.MultipartBody;

public class RegisterForm {

    private String email;
    private String password1;
    private String password2;
    private String last_name;
    private String first_name;
    private String room_number;
    private String campus;
    @Nullable
    private MultipartBody.Part profile_picture;

    // Constructor to add a new product to the VM in the add fragment
    public RegisterForm(String email, String password1, String password2, String last_name, String first_name, String room_number,
                        String campus) {
        this.email = email;
        this.password1 = password1;
        this.password2 = password2;
        this.last_name = last_name;
        this.first_name = first_name;
        this.room_number = room_number;
        this.campus = campus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public MultipartBody.Part getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(MultipartBody.Part profile_picture) {
        this.profile_picture = profile_picture;
    }
}
