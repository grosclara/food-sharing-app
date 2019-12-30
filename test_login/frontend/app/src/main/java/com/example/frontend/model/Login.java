package com.example.frontend.model;

/**
 * Class of the Login that contains a username and a password.
 * This model is used to send an identification object to the database.
 * The defined method are the constructor.
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class Login {
    private String username;
    private String password;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
