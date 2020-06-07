package com.example.cshare.data.sources.factories;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.cshare.data.models.Product;
import com.example.cshare.data.sources.PreferenceProvider;
import com.example.cshare.data.sources.SharedDataSource;

/**
 * Factory class responsible for the storage of a SharedDataSource in a MutableLiveData object.=
 *
 * @see SharedDataSource
 * @see MutableLiveData
 * @see DataSource.Factory
 * @since 2.1
 * @author Clara Gros
 * @author Babacar Toure
 */
public class SharedDataSourceFactory extends DataSource.Factory {

    // Creating the mutable live data
    private MutableLiveData<PageKeyedDataSource<Integer, Product>> sharedProductsLiveDataSource = new MutableLiveData<>();

    private PreferenceProvider prefs;
    private Context context;
    private SharedDataSource sharedProductsDataSource;

    /**
     * Class constructor
     *
     * @param context
     * @param prefs
     */
    public SharedDataSourceFactory(Context context, PreferenceProvider prefs) {
        this.context = context;
        this.prefs = prefs;
    }

    /**
     * Method that retrieves and stores the data source object in a live data object and returns it
     * @return sharedProductsDataSource
     * @see SharedDataSource
     */
    @Override
    public DataSource create() {
        // Getting our data source object
        sharedProductsDataSource = new SharedDataSource(context, prefs.getToken());
        // Posting the datasource to get the values
        sharedProductsLiveDataSource.postValue(sharedProductsDataSource);
        // Returning the datasource
        return sharedProductsDataSource;
    }

    /**
     * Getter for sharedProductsLiveDataSource
     * @return
     */
    public MutableLiveData<PageKeyedDataSource<Integer, Product>> getSharedProductsLiveDataSource() {
        return sharedProductsLiveDataSource;
    }
}