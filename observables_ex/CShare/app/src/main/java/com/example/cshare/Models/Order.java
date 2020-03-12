package com.example.cshare.Models;

public class Order {

    private int id;
    private int client;
    private int product;

    public Order(int client, int product) {
        this.client = client;
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClient() {
        return client;
    }

    public void setClient(int client) {
        this.client = client;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

}
