package com.example.frontend.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

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
    // Its value is automatically set to Available by the server when a product is being created
    private String status;
    // Reference to the (User) supplier id
    private int supplier;
    private String created_at;
    private String updated_at;
    private String product_picture;
    private String category;
    private String quantity;
    private String expiration_date;

    /*
     * Constructor of the Product class.
     * Only take a few attributes in argument because the server auto adds the others (id, created_at, updated_at)
     */

    public Product(int id, String name, String status, String created_at, String updated_at, String product_picture, int supplier, String category, String quantity, String expiration_date){
        this.id = id;
        this.name = name;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.product_picture = product_picture;
        this.supplier = supplier;
        this.category = category;
        this.quantity = quantity;
        this.expiration_date = expiration_date;
    }

    public Product(String status){
        this.status = status;
        // this.supplier = supplier;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
