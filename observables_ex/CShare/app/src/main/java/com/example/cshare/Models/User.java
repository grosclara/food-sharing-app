package com.example.cshare.Models;

//JSON TO POJO ????

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
    private String token;
    private String profile_picture;

    public User(String email, String last_name, String first_name, String password1, String password2, String campus, String room_number) {
        this.email = email;
        this.last_name = last_name;
        this.first_name = first_name;
        this.password1 = password1;
        this.password2 = password2;
        this.campus = campus;
        this.room_number = room_number;
    }

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public User(String oldPassword, String password1, String password2){
        this.new_password1 = password1;
        this.new_password2 = password2;
        this.old_password = oldPassword;
    }

    public User(String email){
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public int getId() {
        return id;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getEmail() {
        return email;
    }

    public String getCampus() {
        return campus;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
