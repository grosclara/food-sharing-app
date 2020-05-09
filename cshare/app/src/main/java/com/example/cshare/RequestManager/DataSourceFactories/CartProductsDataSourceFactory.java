package com.example.cshare.RequestManager.DataSourceFactories;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.cshare.Models.Product;
import com.example.cshare.RequestManager.DataSources.CartProductsDataSource;
import com.example.cshare.RequestManager.DataSources.HomeProductsDataSource;
import com.example.cshare.Utils.PreferenceProvider;

public class CartProductsDataSourceFactory extends DataSource.Factory {

    private PreferenceProvider prefs;
    private Context context;
    private CartProductsDataSource cartProductsDataSource;

    public CartProductsDataSourceFactory(Context context, PreferenceProvider prefs) {
        this.prefs = prefs;
        this.context = context;
    }

    private MutableLiveData<PageKeyedDataSource<Integer, Product>> cartProductsLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource create() {
        cartProductsDataSource = new CartProductsDataSource(context, prefs.getToken());
        cartProductsLiveDataSource.postValue(cartProductsDataSource);
        return cartProductsDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Product>> getCartProductsLiveDataSource() {
        return cartProductsLiveDataSource;
    }
}