package com.example.cshare.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.example.cshare.Models.Product;
import com.example.cshare.RequestManager.DataSourceFactories.SharedProductsDataSourceFactory;
import com.example.cshare.Utils.Constants;
import com.example.cshare.Utils.PreferenceProvider;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SharedViewModel extends AndroidViewModel {

    private SharedProductsDataSourceFactory sharedProductsDataSourceFactory;

    private LiveData<PagedList<Product>> productPagedList;
    private LiveData<PageKeyedDataSource<Integer, Product>> liveDataSource;

    public LiveData<PagedList<Product>> getProductPagedList() {
        return productPagedList;
    }

    public LiveData<PageKeyedDataSource<Integer, Product>> getLiveDataSource() {
        return liveDataSource;
    }

    public SharedViewModel(Application application) throws GeneralSecurityException, IOException {
        super(application);

        sharedProductsDataSourceFactory = new SharedProductsDataSourceFactory(application, new PreferenceProvider(application));
        liveDataSource = sharedProductsDataSourceFactory.getSharedProductsLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setPageSize(Constants.PAGE_SIZE)
                        .setEnablePlaceholders(false)
                        .build();

        productPagedList = (new LivePagedListBuilder<Integer, Product>(sharedProductsDataSourceFactory, config)).build();
    }

    public void refresh(){
        sharedProductsDataSourceFactory.getSharedProductsLiveDataSource().getValue().invalidate();
        //getProductPagedList().getValue().getDataSource().invalidate();
    }
}