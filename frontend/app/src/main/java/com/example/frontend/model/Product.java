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
    /** Contains the id of the User supplier */
    private int supplier;
    private String created_at;
    private String updated_at;
    private String product_picture;

    // Constructor
    public Product(String name, int supplier, String product_picture) {
        this.name = name;
        this.supplier = supplier;
        this.product_picture = product_picture;
    }

    public void setIs_available(Boolean is_available) {this.is_available = is_available;}

    public int getId() { return id; }

    public String getName() {
        return name;
    }

    public int getSupplier() { return supplier; }

    public Boolean getIs_available() {
        return is_available;
    }

    public String getProduct_picture() {
        return product_picture;
    }

    public void setProduct_picture(String product_picture) {
        this.product_picture = product_picture;
    }
}
