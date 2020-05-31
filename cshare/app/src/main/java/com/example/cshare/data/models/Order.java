package com.example.cshare.data.models;

/**
 * Class of the Order table.
 * The attributes defined corresponds to the ones of the remote database.
 * This model is used to facilitate order's CRUD HTTP requests.
 * The defined methods are the constructor and the getters/setters.
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */
public class Order {
    private int product;

    /*
     * Constructor of the Order class.
     */
    public Order(int customerID, int productID) {
        this.product = productID;
    }

    public int getProductID() {
        return product;
    }
}
