package com.example.frontend.model;

/**
 * Class of the Order table.
 * The attributes defined corresponds to the ones of the remote database.
 * This model is used to facilitate order's CRUD HTTP requests.
 * The defined methods are the constructor and the getters/setters.
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class Order {
    private int id;
    // Reference to the id of the client
    private int client;
    // Reference to the id of the product
    private int product;

    /*
     * Constructor of the Order class.
     * Only take a few attributes in argument because the server auto adds the id
     */
    public Order( int client, int product) {
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
