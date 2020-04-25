package com.example.cshare.Models.Forms;

import androidx.annotation.Nullable;

import okhttp3.MultipartBody;

public class EditProfileForm {

    private String last_name;
    private String first_name;
    private String room_number;
    private String campus;
    @Nullable
    private MultipartBody.Part profile_picture;

    // Constructor to add a new product to the VM in the add fragment
    public EditProfileForm(String last_name, String first_name, String room_number, String campus) {
        this.last_name = last_name;
        this.first_name = first_name;
        this.room_number = room_number;
        this.campus = campus;
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

    @Nullable
    public MultipartBody.Part getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(@Nullable MultipartBody.Part profile_picture) {
        this.profile_picture = profile_picture;
    }
}
