package com.example.cshare.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.cshare.Models.Response.ApiEmptyResponse;
import com.example.cshare.Models.Response.ProductResponse;
import com.example.cshare.Models.Response.ResponseProductList;
import com.example.cshare.Models.Order;
import com.example.cshare.Models.Product;
import com.example.cshare.Models.ProductForm;
import com.example.cshare.RequestManager.ProductRequestManager;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

public class ProductViewModel extends AndroidViewModel {

    private ProductRequestManager productRequestManager;

    // MutableLiveData object that contains the list of products
    private MutableLiveData<ResponseProductList> availableProductList;
    private MutableLiveData<ResponseProductList> sharedProductList;
    private MutableLiveData<ResponseProductList> inCartProductList;
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
        availableProductList = productRequestManager.getAvailableProductList();
        inCartProductList = productRequestManager.getInCartProductList();
        sharedProductList = productRequestManager.getSharedProductList();
        addProductResponse = productRequestManager.getAddProductResponse();
        deleteProductResponse = productRequestManager.getDeleteProductResponse();
        cancelOrderResponse = productRequestManager.getCancelOrderResponse();
        deliverProductResponse = productRequestManager.getDeliverProductResponse();
        orderProductResponse = productRequestManager.getOrderProductResponse();
    }

    // Getter method
    public MutableLiveData<ResponseProductList> getAvailableProductList() { return availableProductList; }
    public MutableLiveData<ResponseProductList> getInCartProductList() {
        return inCartProductList;
    }
    public MutableLiveData<ResponseProductList> getSharedProductList() {
        return sharedProductList;
    }
    public MutableLiveData<ProductResponse> getAddProductResponse() { return addProductResponse; }
    public MutableLiveData<ProductResponse> getDeleteProductResponse() { return deleteProductResponse; }
    public MutableLiveData<ProductResponse> getCancelOrderResponse() { return cancelOrderResponse; }
    public MutableLiveData<ProductResponse> getDeliverProductResponse() { return deliverProductResponse; }

    public MutableLiveData<ProductResponse> getOrderProductResponse() { return orderProductResponse; }

    // Update products in request manager
    public void update() {
        productRequestManager.update();
    }

    // Add a product
    public void addProduct(ProductForm productToPost, Product product) {
        productRequestManager.addProduct(productToPost, product);
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
