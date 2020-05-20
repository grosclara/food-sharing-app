package com.example.cshare.ui.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.cshare.data.apiresponses.ProductResponse;
import com.example.cshare.data.models.Order;
import com.example.cshare.data.models.Product;
import com.example.cshare.data.sources.ProductRequestManager;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * ViewModel class responsible for preparing and managing the product operations-related data for
 * an Activity or a Fragment.
 * <p>
 * This class provides one the one hand getter methods to retrieve data from the attributes.
 * One the other hand, it provides methods that will directly call request manager methods to
 * perform some actions on the data.
 *
 * @see AndroidViewModel
 * @see MutableLiveData
 * @see ProductRequestManager
 * @since 2.0
 * @author Clara Gros
 * @author Babacar Toure
 */
public class ProductViewModel extends AndroidViewModel {

    /**
     * Repository that will fetch the product-related data from the remote API source
     */
    private ProductRequestManager productRequestManager;

    // MutableLiveData object that contains the list of products
    private MutableLiveData<ProductResponse> addProductResponse;
    private MutableLiveData<ProductResponse> deleteProductResponse;
    private MutableLiveData<ProductResponse> cancelOrderResponse;
    private MutableLiveData<ProductResponse> deliverProductResponse;
    private MutableLiveData<ProductResponse> orderProductResponse;

    /**
     * Constructor of the ViewModel.
     * Takes in an Application parameter that is provided to retrieve the request manager via the
     * getInstance method.
     *
     * @param application
     * @throws GeneralSecurityException
     * @throws IOException
     * @see ProductRequestManager#getInstance(Application)
     */
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

    /**
     * Calls the addProduct method of the request manager
     *
     * @param product (Product)
     * @see ProductRequestManager#addProduct(Product)
     */
    public void addProduct(Product product) {
        productRequestManager.addProduct(product);
    }
    /**
     * Calls the order method of the  product request manager
     *
     * @param request (Order)
     * @see ProductRequestManager#order(Order)
     */
    public void order(Order request){
        productRequestManager.order(request);
    }
    /**
     * Calls the deliver method of the product request manager
     *
     * @param productID (int)
     * @see ProductRequestManager#deliver(int) 
     */
    public void deliver(int productID){
        productRequestManager.deliver(productID);
    }
    /**
     * Calls the cancelOrder method of the product request manager
     *
     * @param productID (int)
     * @see ProductRequestManager#cancelOrder(int)
     */
    public void cancelOrder(int productID){
        productRequestManager.cancelOrder(productID);
    }
    /**
     * Calls the deleteProduct method of the product request manager
     *
     * @param productID (int)
     * @see ProductRequestManager#deleteProduct(int)  
     */
    public void deleteProduct(int productID){
        productRequestManager.deleteProduct(productID);
    }
}
