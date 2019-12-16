package com.example.frontend.model;

/**
 * Class of the <b>User</b> table. Each attribute corresponds to a column of the table and the defined methods are the getters.
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class User {

    private int id;
    private String name;
    private String first_name;
    private String profile_picture_url;


    public String getName() {
        return name;
    }

    public String getFirstName() {
        return first_name;
    }

    public int getId() {
        return id;
    }
}