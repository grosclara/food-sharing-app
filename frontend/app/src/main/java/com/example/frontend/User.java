package com.example.frontend;

import com.google.gson.annotations.SerializedName;

public class User {

    private int id;
    private String name;
    private String first_name;

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
