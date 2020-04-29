package com.example.cshare.RequestManager.DataSourceFactories;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.cshare.Models.Product;
import com.example.cshare.RequestManager.DataSources.SharedProductsDataSource;

public class SharedProductsDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Product>> sharedProductsLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource create() {
        SharedProductsDataSource sharedProductsDataSource = new SharedProductsDataSource();
        sharedProductsLiveDataSource.postValue(sharedProductsDataSource);
        return sharedProductsDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Product>> getSharedProductsLiveDataSource() {
        return sharedProductsLiveDataSource;
    }
}