package com.example.cshare.data.models;

import androidx.annotation.Nullable;

import okhttp3.MultipartBody;

/**
 * Class of the Product table.
 * <p>
 * The attributes defined correspond to the ones of the remote database.
 * This model is used to facilitate products' CRUD HTTP requests.
 * The defined methods are the constructor and the getters/setters.
 * @author Clara Gros
 * @author Babacar Toure
 * @version 1.0
 */
public class Product {

    private int id;
    /**
     * Product image to upload to the server
     */
    @Nullable
    private MultipartBody.Part product_picture_body;
    private String name;
    private String status;
    private int supplier;
    private String product_picture;
    private String category;
    private String quantity;
    private String expiration_date;
    private String campus;
    private String room_number;

    /*
     * Constructor of the Product class.
     *
     * Used to create a new product and add it to the remote database
     */
    public Product(MultipartBody.Part productPictureBody, String productName, String productCategory, String quantity, String expirationDate, int supplierID, String productPicture, String campus, String room_number) {
        this.product_picture_body = productPictureBody;
        this.name = productName;
        this.category = productCategory;
        this.quantity = quantity;
        this.expiration_date = expirationDate;
        this.product_picture = productPicture;
        this.supplier = supplierID;
        this.room_number = room_number;
        this.campus = campus;
    }

    public String getCampus() { return campus; }

    public void setCampus(String campus) { this.campus = campus; }

    public String getCategory() {
        return category;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getExpiration_date() {
        return expiration_date;
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

    public String getProduct_picture() {
        return product_picture;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Nullable
    public MultipartBody.Part getProduct_picture_body() {
        return product_picture_body;
    }
}
