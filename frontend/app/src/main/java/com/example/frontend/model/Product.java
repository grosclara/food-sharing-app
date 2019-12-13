package com.example.frontend.model;

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
    private String product_image_url;

    // Constructor
    public Product(String name, Boolean is_available, int offerer, String product_image_url) {
        this.name = name;
        this.is_available = is_available;
        this.offerer = offerer;
        this.product_image_url = product_image_url;
    }

    public int getId() { return id; }

    public String getName() {
        return name;
    }

    public int getOfferer() { return offerer; }

    public Boolean getIs_available() {
        return is_available;
    }

    public String getProduct_image_url() {
        return product_image_url;
    }
}
