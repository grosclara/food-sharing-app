package com.example.cshare.data.models;

import androidx.annotation.Nullable;

import okhttp3.MultipartBody;

public class Product {

    private int id;
    @Nullable
    private MultipartBody.Part product_picture_body;
    private String name;
    private String status;
    private int supplier;
    private String created_at;
    private String updated_at;
    private String product_picture;
    private String category;
    private String quantity;
    private String expiration_date;
    private String campus;
    private String room_number;

    // Constructor to add a new product to the VM in the add fragment
    public Product(String name, String status, String product_picture, String category, String quantity, String expiration_date, String campus, String room_number){
        this.name = name;
        this.status = status;
        this.product_picture = product_picture;
        this.category = category;
        this.quantity = quantity;
        this.expiration_date = expiration_date;
        this.campus = campus;
        this.room_number = room_number;
    }

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

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
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

    @Nullable
    public MultipartBody.Part getProduct_picture_body() {
        return product_picture_body;
    }

    public void setProduct_picture_body(@Nullable MultipartBody.Part product_picture_body) {
        this.product_picture_body = product_picture_body;
    }
}
