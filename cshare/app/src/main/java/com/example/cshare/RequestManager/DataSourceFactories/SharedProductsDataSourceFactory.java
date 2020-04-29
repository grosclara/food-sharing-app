package com.example.cshare.RequestManager.DataSourceFactories;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.cshare.Models.Product;
import com.example.cshare.RequestManager.DataSources.SharedProductsDataSource;
import com.example.cshare.Utils.PreferenceProvider;

public class SharedProductsDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Product>> sharedProductsLiveDataSource = new MutableLiveData<>();

    private PreferenceProvider prefs;
    private Context context;

    public SharedProductsDataSourceFactory(Context context, PreferenceProvider prefs) {
        this.context = context;
        this.prefs = prefs;
    }

    @Override
    public DataSource create() {
        SharedProductsDataSource sharedProductsDataSource = new SharedProductsDataSource(context, prefs.getToken());
        sharedProductsLiveDataSource.postValue(sharedProductsDataSource);
        return sharedProductsDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Product>> getSharedProductsLiveDataSource() {
        return sharedProductsLiveDataSource;
    }
}