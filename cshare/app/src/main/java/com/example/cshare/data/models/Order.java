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
    private int customer;
    private int product;

    /*
     * Constructor of the Order class.
     * Only take a few attributes in argument because the server auto adds the id
     */

    public Order(int customerID, int productID) {
        this.customer = customerID;
        this.product = productID;
    }

    public int getProductID() {
        return product;
    }
}
