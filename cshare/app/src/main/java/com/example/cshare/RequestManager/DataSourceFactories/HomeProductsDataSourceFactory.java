package com.example.cshare.RequestManager.DataSourceFactories;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.cshare.Models.Product;
import com.example.cshare.RequestManager.DataSources.HomeProductsDataSource;

public class HomeProductsDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Product>> homeProductsLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource create() {
        HomeProductsDataSource homeProductsDataSource = new HomeProductsDataSource();
        homeProductsLiveDataSource.postValue(homeProductsDataSource);
        return homeProductsDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Product>> getHomeProductsLiveDataSource() {
        return homeProductsLiveDataSource;
    }
}
