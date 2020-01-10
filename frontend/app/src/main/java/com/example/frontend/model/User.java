package com.example.frontend.model;

/**
 * Class of the User table.
 * The attributes defined corresponds to the ones of the remote database.
 * This model is used to facilitate user's CRUD HTTP requests.
 * The defined methods are the constructor and the getters/setters.
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class User {

    private int id;
    private String email;
    private String password;
    private String name;
    private String first_name;
    private String token;
    // Correspond to the url of the picture in the server (ex: "http://127.0.0.1:8000/media/user/android.png/")
    private String profile_picture_url;
    private String created_at;
    private String updated_at;

    /*
     * Constructor of the User class.
     * Only take a few attributes in argument because the server auto adds the others (id, created_at, updated_at)
     */
    public User(String email, String name, String first_name, String password) {
        this.email = email;
        this.name = name;
        this.first_name = first_name;
        this.password = password;
    }

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }


    public String getToken() {
        return token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getProfile_picture_url() {
        return profile_picture_url;
    }

    public void setProfile_picture_url(String profile_picture_url) {
        this.profile_picture_url = profile_picture_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}