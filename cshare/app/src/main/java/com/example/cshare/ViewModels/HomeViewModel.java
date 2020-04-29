package com.example.cshare.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.example.cshare.Models.Product;
import com.example.cshare.RequestManager.DataSourceFactories.HomeProductsDataSourceFactory;

public class HomeViewModel extends ViewModel {
    private LiveData<PagedList<Product>> productPagedList;
    private LiveData<PageKeyedDataSource<Integer, Product>> liveDataSource;

    public LiveData<PagedList<Product>> getProductPagedList() {
        return productPagedList;
    }

    public LiveData<PageKeyedDataSource<Integer, Product>> getLiveDataSource() {
        return liveDataSource;
    }

    public HomeViewModel() {
        HomeProductsDataSourceFactory homeProductsDataSourceFactory = new HomeProductsDataSourceFactory();
        liveDataSource = homeProductsDataSourceFactory.getHomeProductsLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setPageSize(15)
                        .setEnablePlaceholders(false)
                        .build();

        productPagedList = (new LivePagedListBuilder<Integer, Product>(homeProductsDataSourceFactory, config)).build();
    }
}
