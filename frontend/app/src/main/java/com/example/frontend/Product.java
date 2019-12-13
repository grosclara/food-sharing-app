package com.example.frontend;

import java.io.Serializable;

/**
 * Class of the <b>Product</b> table. Each attribute corresponds to a column of the table and the defined methods are the getters.
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class Product implements Serializable {

    private int id;
    private String name;
    private Boolean is_available;
    /** Contains the id of the User offerer */
    private int offerer;

    // Constructor
    public Product(String name, Boolean is_available, int offerer) {
        this.name = name;
        this.is_available = is_available;
        this.offerer = offerer;
    }

    public int getId() { return id; }

    public String getName() {
        return name;
    }

    public Boolean getIsAvailable() {
        return is_available;
    }

    public int getOfferer() { return offerer; }
}
