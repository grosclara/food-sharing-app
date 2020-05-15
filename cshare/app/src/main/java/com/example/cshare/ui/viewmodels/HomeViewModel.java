package com.example.cshare.ui.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.example.cshare.data.models.Product;
import com.example.cshare.data.sources.factories.HomeDataSourceFactory;
import com.example.cshare.utils.Constants;
import com.example.cshare.data.sources.PreferenceProvider;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class HomeViewModel extends AndroidViewModel {

    private HomeDataSourceFactory homeProductsDataSourceFactory;

    private LiveData<PagedList<Product>> productPagedList;
    private LiveData<PageKeyedDataSource<Integer, Product>> liveDataSource;

    public LiveData<PagedList<Product>> getProductPagedList() {
        return productPagedList;
    }

    public LiveData<PageKeyedDataSource<Integer, Product>> getLiveDataSource() {
        return liveDataSource;
    }

    public HomeViewModel(Application application) throws GeneralSecurityException, IOException {
        super(application);

        homeProductsDataSourceFactory = new HomeDataSourceFactory(application, new PreferenceProvider(application));
        liveDataSource = homeProductsDataSourceFactory.getHomeProductsLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setPageSize(Constants.PAGE_SIZE)
                        .setEnablePlaceholders(false)
                        .build();
        productPagedList = (new LivePagedListBuilder<Integer, Product>(homeProductsDataSourceFactory, config)).build();
    }

    public void refresh(){
        if(homeProductsDataSourceFactory.getHomeProductsLiveDataSource().getValue() != null) {
            homeProductsDataSourceFactory.getHomeProductsLiveDataSource().getValue().invalidate();
        }
    }
}