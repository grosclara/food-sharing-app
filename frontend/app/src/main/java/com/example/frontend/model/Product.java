package com.example.frontend.model;

import java.io.Serializable;

/**
 * Class of the Product table that implements Serializable.
 * The attributes defined corresponds to the ones of the remote database.
 * This model is used to facilitate products' CRUD HTTP requests.
 * The defined methods are the constructor and the getters/setters.
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class Product implements Serializable {

    private int id;
    private String name;
    // Its value is automatically set to True by the server when a product is being created
    private Boolean is_available;
    // Reference to the (User) supplier id
    private int supplier;
    private String created_at;
    private String updated_at;
    private String product_picture;
    /*
     * Constructor of the Product class.
     * Only take a few attributes in argument because the server auto adds the others (id, is_available, created_at, updated_at)
     */
    public Product(String name, int supplier) {
        this.name = name;
        this.supplier = supplier;
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

    public Boolean getIs_available() {
        return is_available;
    }

    public void setIs_available(Boolean is_available) {
        this.is_available = is_available;
    }

    public int getSupplier() {
        return supplier;
    }

    public void setSupplier(int supplier) {
        this.supplier = supplier;
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

    public String getProduct_picture() {
        return product_picture;
    }

    public void setProduct_picture(String product_picture) {
        this.product_picture = product_picture;
    }
}
