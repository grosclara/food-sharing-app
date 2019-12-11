package com.example.frontend;

/**
 * Class of the <b>Product</b> table. Each attribute corresponds to a column of the table and the defined methods are the getters.
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class Product {

    private int id;
    private String name;
    private Boolean is_available;
    /** Contains the URL to the User offerer */
    private String offerer;

    // Constructor
    public Product(String name, Boolean is_available, String offerer) {
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

    public String getOfferer() { return offerer; }
}
