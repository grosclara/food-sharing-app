package com.example.cshare.Models;

import okhttp3.MultipartBody;

public class ProductToPost {

    // Constructor of a created product that fit the post HTTP required format

    private MultipartBody.Part productPicture;
    private String productName;
    private String productCategory;
    private String quantity;
    private String expirationDate;
    private int supplierID;

    public ProductToPost(MultipartBody.Part productPicture, String productName, String productCategory, String quantity, String expirationDate, int supplierID) {
        this.productPicture = productPicture;
        this.productName = productName;
        this.productCategory = productCategory;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.supplierID = supplierID;
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
}
