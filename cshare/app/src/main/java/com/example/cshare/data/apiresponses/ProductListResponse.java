package com.example.cshare.data.apiresponses;

import com.example.cshare.data.models.Product;

import java.util.List;

/**
 * Class that contains the response to a request returning a paginated product list.
 * <p>
 * The defined methods are the getters.
 *
 * @see Product
 * @since 2.0
 * @author Clara Gros
 * @author Babacar Toure
 */
public class ProductListResponse {

    private List<Product> results;
    private int count;
    private String previous;
    private String next;

    /**
     * Class constructor
     *
     * @param productList
     * @param count
     * @param previous
     * @param next
     */
    public ProductListResponse(List<Product> productList, int count, String previous, String next) {
        this.results = productList;
        this.count = count;
        this.previous = previous;
        this.next = next;
    }

    public List<Product> getProductList() { return results; }
    public String getNext() { return next; }
}

