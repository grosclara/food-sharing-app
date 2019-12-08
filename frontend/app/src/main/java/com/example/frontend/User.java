package com.example.frontend;

import com.google.gson.annotations.SerializedName;

public class User {
    private String name;
    @SerializedName("first_name")
    private String firstName;

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }
}
