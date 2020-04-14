package com.example.cshare.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cshare.Models.Order;
import com.example.cshare.Models.Product;
import com.example.cshare.Models.ProductForm;
import com.example.cshare.Models.ProductToPost;
import com.example.cshare.RequestManager.ProductRequestManager;

import java.util.List;
import java.util.Map;

public class ProductViewModel extends ViewModel {

    private ProductRequestManager productRequestManager;

    // MutableLiveData object that contains the list of products
    private MutableLiveData<List<Product>> availableProductList = new MutableLiveData<>();
    private MutableLiveData<List<Product>> sharedProductList = new MutableLiveData<>();
    private MutableLiveData<List<Product>> inCartProductList = new MutableLiveData<>();

    public ProductViewModel() {

        // Get request manager instance
        productRequestManager = ProductRequestManager.getInstance();
        // Retrieve product lists from request manager
        availableProductList = productRequestManager.getAvailableProductList();
        inCartProductList = productRequestManager.getInCartProductList();
        sharedProductList = productRequestManager.getSharedProductList();

    }

    // Get request manager
    public ProductRequestManager getProductRequestManager() {
        return productRequestManager;
    }

    // Getter method
    public MutableLiveData<List<Product>> getAvailableProductList() {
        return availableProductList;
    }

    public MutableLiveData<List<Product>> getInCartProductList() {
        return inCartProductList;
    }

    public MutableLiveData<List<Product>> getSharedProductList() {
        return sharedProductList;
    }

    // Update products in request manager
    public void update() {
        productRequestManager.updateRequestManager();
    }

    // Add a product and update every list
    public void addProduct(ProductForm productToPost, Product product) {
        productRequestManager.addProduct(productToPost, product);
        //update();
    }

    public void order(Order request, Map status){
        productRequestManager.order(request, status);
    }

    public void deliver(int productID, Map status){
        productRequestManager.deliver(productID, status);
    }

    public void cancelOrder(int productID, Map status){
        productRequestManager.cancelOrder(productID, status);
    }

    public void deleteProduct(Product product){
        productRequestManager.deleteProduct(product);
    }
}
