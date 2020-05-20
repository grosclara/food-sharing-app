package com.example.cshare.ui.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.cshare.data.models.Product;
import com.example.cshare.data.sources.PreferenceProvider;
import com.example.cshare.data.sources.factories.SharedDataSourceFactory;
import com.example.cshare.utils.Constants;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * ViewModel class responsible for preparing and managing the list of in cart products data for
 * the HomeScreenActivity or the SharedFragment.
 * <p>
 * This class provides one the one hand getter methods to retrieve data from the attributes.
 * One the other hand, it provides methods that will directly call request manager methods to
 * perform some actions on the data.
 *
 * @see AndroidViewModel
 * @see com.example.cshare.ui.views.HomeScreenActivity
 * @see com.example.cshare.ui.views.productlists.SharedFragment
 * @see SharedDataSourceFactory
 * @see LiveData
 * @see PagedList
 * @since 2.1
 * @author Clara Gros
 * @author Babacar Toure
 */
public class SharedViewModel extends AndroidViewModel {

    private SharedDataSourceFactory sharedDataSourceFactory;
    /**
     *  Collection wrapped in a LiveData object that loads data in pages, asynchronously.
     *  It is used to load available product list from our remote source and present it easily
     *  in your UI within our RecyclerView.
     */
    private LiveData<PagedList<Product>> productPagedList;

    /**
     * Class constructor
     * <p>
     * First gets an instance of the DataSourceFactory class and configures the PagedList.config.
     * Then initializes the pageList using the created config and DataSourceFactory
     *
     * @see Application
     * @see com.example.cshare.data.sources.factories.SharedDataSourceFactory
     * @see PagedList.Config
     * @see PagedList
     */
    public SharedViewModel(Application application) throws GeneralSecurityException, IOException {
        super(application);
        sharedDataSourceFactory = new SharedDataSourceFactory(application, new PreferenceProvider(application));

        // Configure the pagination
        PagedList.Config config =
                (new PagedList.Config.Builder())
                        // Number of items to load in the PagedList.
                        .setPageSize(Constants.PAGE_SIZE)
                        // Enabling placeholders mean there is a placeholder that is visible to the
                        // user till the data is fully loaded.
                        .setEnablePlaceholders(false)
                        .build();
        // Builds a LiveData<PagedList>, based on DataSource.Factory and a PagedList.Config.
        productPagedList = (new LivePagedListBuilder<Integer, Product>(sharedDataSourceFactory, config)).build();
    }

    /**
     * Getter method for the pagedList
     */
    public LiveData<PagedList<Product>> getProductPagedList() {
        return productPagedList;
    }

    /**
     * Reloads all content to get a new version of data, by calling invalidate
     * on the current DataSource.
     *
     * @see DataSource#invalidate()
     */
    public void refresh(){
        if (sharedDataSourceFactory.getSharedProductsLiveDataSource().getValue() != null){
            sharedDataSourceFactory.getSharedProductsLiveDataSource().getValue().invalidate();
        }
    }
}