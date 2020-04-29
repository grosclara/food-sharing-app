package com.example.cshare.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.cshare.Models.ApiResponses.ProductResponse;
import com.example.cshare.Models.ApiResponses.ProductListResponse;
import com.example.cshare.Models.Order;
import com.example.cshare.Models.Product;
import com.example.cshare.RequestManager.ProductRequestManager;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

public class ProductViewModel extends AndroidViewModel {

    private ProductRequestManager productRequestManager;

    // MutableLiveData object that contains the list of products
    private MutableLiveData<ProductResponse> addProductResponse;
    private MutableLiveData<ProductResponse> deleteProductResponse;
    private MutableLiveData<ProductResponse> cancelOrderResponse;
    private MutableLiveData<ProductResponse> deliverProductResponse;
    private MutableLiveData<ProductResponse> orderProductResponse;

    public ProductViewModel(Application application) throws GeneralSecurityException, IOException {
        super(application);

        // Get request manager instance
        productRequestManager = ProductRequestManager.getInstance(application);
        // Retrieve product lists from request manager
        addProductResponse = productRequestManager.getAddProductResponse();
        deleteProductResponse = productRequestManager.getDeleteProductResponse();
        cancelOrderResponse = productRequestManager.getCancelOrderResponse();
        deliverProductResponse = productRequestManager.getDeliverProductResponse();
        orderProductResponse = productRequestManager.getOrderProductResponse();
    }

    // Getter method
    public MutableLiveData<ProductResponse> getAddProductResponse() { return addProductResponse; }
    public MutableLiveData<ProductResponse> getDeleteProductResponse() { return deleteProductResponse; }
    public MutableLiveData<ProductResponse> getCancelOrderResponse() { return cancelOrderResponse; }
    public MutableLiveData<ProductResponse> getDeliverProductResponse() { return deliverProductResponse; }

    public MutableLiveData<ProductResponse> getOrderProductResponse() { return orderProductResponse; }

    // Add a product
    public void addProduct(Product product) {
        productRequestManager.addProduct(product);
    }

    public void order(Order request){
        productRequestManager.order(request);
    }

    public void deliver(int productID){
        productRequestManager.deliver(productID);
    }

    public void cancelOrder(int productID){
        productRequestManager.cancelOrder(productID);
    }

    public void deleteProduct(Product product){
        productRequestManager.deleteProduct(product);
    }
}
