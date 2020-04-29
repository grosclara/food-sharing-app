package com.example.cshare.RequestManager.DataSourceFactories;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.cshare.Models.Product;
import com.example.cshare.RequestManager.DataSources.HomeProductsDataSource;
import com.example.cshare.RequestManager.ProductRequestManager;
import com.example.cshare.Utils.PreferenceProvider;

public class HomeProductsDataSourceFactory extends DataSource.Factory {

    private PreferenceProvider prefs;
    private Context context;

    public HomeProductsDataSourceFactory(Context context, PreferenceProvider prefs) {
        this.prefs = prefs;
        this.context = context;
    }

    private MutableLiveData<PageKeyedDataSource<Integer, Product>> homeProductsLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource create() {
        HomeProductsDataSource homeProductsDataSource = new HomeProductsDataSource(context, prefs.getToken());
        homeProductsLiveDataSource.postValue(homeProductsDataSource);
        return homeProductsDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Product>> getHomeProductsLiveDataSource() {
        return homeProductsLiveDataSource;
    }
}
