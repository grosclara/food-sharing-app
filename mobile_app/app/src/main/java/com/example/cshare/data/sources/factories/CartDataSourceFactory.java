package com.example.cshare.data.sources.factories;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.cshare.data.models.Product;
import com.example.cshare.data.sources.CartDataSource;
import com.example.cshare.data.sources.PreferenceProvider;

/**
 * Factory class responsible for the storage of a CartDataSource in a MutableLiveData object.=
 *
 * @see CartDataSource
 * @see MutableLiveData
 * @see DataSource.Factory
 * @since 2.1
 * @author Clara Gros
 * @author Babacar Toure
 */
public class CartDataSourceFactory extends DataSource.Factory {

    private PreferenceProvider prefs;
    private Context context;
    private CartDataSource cartProductsDataSource;

    /**
     * Class constructor
     *
     * @param context
     * @param prefs
     */
    public CartDataSourceFactory(Context context, PreferenceProvider prefs) {
        this.prefs = prefs;
        this.context = context;
    }

    // Creating the mutable live data
    private MutableLiveData<PageKeyedDataSource<Integer, Product>> cartProductsLiveDataSource =
            new MutableLiveData<>();

    /**
     * Method that retrieves and stores the data source object in a live data object and returns it
     * @return cartProductsDataSource
     * @see CartDataSource
     */
    @Override
    public DataSource create() {
        // Getting our data source object
        cartProductsDataSource = new CartDataSource(context, prefs.getToken());
        // Posting the datasource to get the values
        cartProductsLiveDataSource.postValue(cartProductsDataSource);
        // Returning the datasource
        return cartProductsDataSource;
    }

    /**
     * Getter for cartProductsLiveDataSource
     * @return
     */
    public MutableLiveData<PageKeyedDataSource<Integer, Product>> getCartProductsLiveDataSource() {
        return cartProductsLiveDataSource;
    }
}