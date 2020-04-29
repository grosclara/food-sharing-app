package com.example.cshare.RequestManager.DataSourceFactories;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.cshare.Models.Product;
import com.example.cshare.RequestManager.DataSources.CartProductsDataSource;

public class CartProductsDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Product>> cartProductsLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource create() {
        CartProductsDataSource cartProductsDataSource = new CartProductsDataSource();
        cartProductsLiveDataSource.postValue(cartProductsDataSource);
        return cartProductsDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Product>> getCartProductsLiveDataSource() {
        return cartProductsLiveDataSource;
    }
}