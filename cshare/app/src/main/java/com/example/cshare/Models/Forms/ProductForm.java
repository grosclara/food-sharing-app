package com.example.cshare.Models.Forms;

import okhttp3.MultipartBody;

public class ProductForm {

    // Constructor of a created product that fit the post HTTP required format

    private String token;
    private MultipartBody.Part productPicture;
    private String productName;
    private String productCategory;
    private String quantity;
    private String expirationDate;
    private int supplierID;

    public ProductForm(MultipartBody.Part productPicture, String productName, String productCategory, String quantity, String expirationDate) {
        this.productPicture = productPicture;
        this.productName = productName;
        this.productCategory = productCategory;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
    }

    public MultipartBody.Part getProductPicture() {
        return productPicture;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID){ this.supplierID = supplierID;}

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
