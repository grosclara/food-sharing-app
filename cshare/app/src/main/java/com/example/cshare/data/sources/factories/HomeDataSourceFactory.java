package com.example.cshare.data.sources.factories;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.cshare.data.models.Product;
import com.example.cshare.data.sources.HomeDataSource;
import com.example.cshare.data.sources.PreferenceProvider;

/**
 * Factory class responsible for the storage of a CartDataSource in a MutableLiveData object.=
 *
 * @see com.example.cshare.data.sources.HomeDataSource
 * @see androidx.lifecycle.MutableLiveData
 * @see androidx.paging.DataSource.Factory
 * @since 2.1
 * @author Clara Gros
 * @author Babacar Toure
 */
public class HomeDataSourceFactory extends DataSource.Factory {

    private PreferenceProvider prefs;
    private Context context;
    private HomeDataSource homeProductsDataSource;

    /**
     * Class constructor
     *
     * @param context
     * @param prefs
     */
    public HomeDataSourceFactory(Context context, PreferenceProvider prefs) {
        this.prefs = prefs;
        this.context = context;
    }

    // Creating the mutable live data
    private MutableLiveData<PageKeyedDataSource<Integer, Product>> homeProductsLiveDataSource = new MutableLiveData<>();

    /**
     * Method that retrieves and stores the data source object in a live data object and returns it
     * @return homeProductsDataSource
     * @see HomeDataSource
     */
    @Override
    public DataSource create() {
        // Getting our data source object
        homeProductsDataSource = new HomeDataSource(context, prefs.getToken());
        // Posting the datasource to get the values
        homeProductsLiveDataSource.postValue(homeProductsDataSource);
        // Returning the datasource
        return homeProductsDataSource;
    }

    /**
     * Getter for homeProductsLiveDataSource
     * @return
     */
    public MutableLiveData<PageKeyedDataSource<Integer, Product>> getHomeProductsLiveDataSource() {
        return homeProductsLiveDataSource;
    }
}
