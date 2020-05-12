package com.example.cshare.ui.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.example.cshare.data.models.Product;
import com.example.cshare.data.sources.factories.SharedDataSourceFactory;
import com.example.cshare.utils.Constants;
import com.example.cshare.data.sources.PreferenceProvider;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SharedViewModel extends AndroidViewModel {

    private SharedDataSourceFactory sharedDataSourceFactory;

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

        sharedDataSourceFactory = new SharedDataSourceFactory(application, new PreferenceProvider(application));
        liveDataSource = sharedDataSourceFactory.getSharedProductsLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setPageSize(Constants.PAGE_SIZE)
                        .setEnablePlaceholders(false)
                        .build();

        productPagedList = (new LivePagedListBuilder<Integer, Product>(sharedDataSourceFactory, config)).build();
    }

    public void refresh(){
        if (sharedDataSourceFactory.getSharedProductsLiveDataSource().getValue() != null){
            sharedDataSourceFactory.getSharedProductsLiveDataSource().getValue().invalidate();
        }
    }
}