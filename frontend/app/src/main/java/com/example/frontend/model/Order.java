package com.example.frontend.model;




/**
 * Class of the <b>Order</b> table. Each attribute corresponds to a column of the table and the defined methods are the getters.
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class Order {
    private int id;
    private int client;
    private int productID;

    //constructor
    public Order( int client, int productID) {
        this.client = client;
        this.productID = productID;
    }

    public int getId() {
        return id;
    }

    public int getClient() {
        return client;
    }

    public int getProductID() {
        return productID;
    }

}
