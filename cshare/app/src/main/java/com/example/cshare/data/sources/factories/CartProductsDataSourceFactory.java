package com.example.cshare.data.sources.factories;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.cshare.data.models.Product;
import com.example.cshare.data.sources.CartProductsDataSource;
import com.example.cshare.data.sources.PreferenceProvider;

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