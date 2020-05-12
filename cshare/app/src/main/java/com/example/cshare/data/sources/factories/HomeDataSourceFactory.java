package com.example.cshare.data.sources.factories;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.cshare.data.models.Product;
import com.example.cshare.data.sources.HomeDataSource;
import com.example.cshare.data.sources.PreferenceProvider;

public class HomeDataSourceFactory extends DataSource.Factory {

    private PreferenceProvider prefs;
    private Context context;
    private HomeDataSource homeProductsDataSource;

    public HomeDataSourceFactory(Context context, PreferenceProvider prefs) {
        this.prefs = prefs;
        this.context = context;
    }

    private MutableLiveData<PageKeyedDataSource<Integer, Product>> homeProductsLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource create() {
        homeProductsDataSource = new HomeDataSource(context, prefs.getToken());
        homeProductsLiveDataSource.postValue(homeProductsDataSource);
        return homeProductsDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Product>> getHomeProductsLiveDataSource() {
        return homeProductsLiveDataSource;
    }
}
