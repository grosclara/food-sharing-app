package com.example.cshare.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.example.cshare.Models.Product;
import com.example.cshare.RequestManager.DataSourceFactories.CartProductsDataSourceFactory;

public class CartViewModel extends ViewModel {

    private CartProductsDataSourceFactory cartProductsDataSourceFactory;

    private LiveData<PagedList<Product>> productPagedList;
    private LiveData<PageKeyedDataSource<Integer, Product>> liveDataSource;

    public LiveData<PagedList<Product>> getProductPagedList() {
        return productPagedList;
    }

    public LiveData<PageKeyedDataSource<Integer, Product>> getLiveDataSource() {
        return liveDataSource;
    }

    public CartViewModel() {
        cartProductsDataSourceFactory = new CartProductsDataSourceFactory();
        liveDataSource = cartProductsDataSourceFactory.getCartProductsLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setPageSize(15)
                        .setEnablePlaceholders(false)
                        .build();

        productPagedList = (new LivePagedListBuilder<Integer, Product>(cartProductsDataSourceFactory, config)).build();
    }

    public void refresh(){
        cartProductsDataSourceFactory.getCartProductsLiveDataSource().getValue().invalidate();
    }
}
