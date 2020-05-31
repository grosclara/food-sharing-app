package com.example.cshare.data.sources.factories;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.cshare.data.models.Product;
import com.example.cshare.data.sources.SharedDataSource;
import com.example.cshare.data.sources.PreferenceProvider;

/**
 * Factory class responsible for the creation of a SharedDataSource.
 *
 * @see SharedDataSource
 * @see DataSource.Factory
 * @since 2.1
 * @author Clara Gros
 * @author Babacar Toure
 */
public class SharedDataSourceFactory extends DataSource.Factory {

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

    @Override
    public DataSource create() {

        sharedProductsDataSource = new SharedDataSource(context, prefs.getToken());
        sharedProductsLiveDataSource.postValue(sharedProductsDataSource);
        return sharedProductsDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Product>> getSharedProductsLiveDataSource() {
        return sharedProductsLiveDataSource;
    }
}