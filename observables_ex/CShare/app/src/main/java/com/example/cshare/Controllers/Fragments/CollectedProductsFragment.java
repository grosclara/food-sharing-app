package com.example.cshare.Controllers.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cshare.Models.Product;
import com.example.cshare.R;
import com.example.cshare.Utils.ApiStreams;

import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class CollectedProductsFragment extends ProductListFragment {

    @Override
    protected BaseFragment newInstance() {
        return new CollectedProductsFragment();
    }

    // Execute our stream
    // Creation of a private method that will contain the call of our stream, and especially its
    // subscription and therefore its execution.
    @Override
    protected void HttpRequest() {

        // Update the GUI to indicate to the user that a network request is running.
        // this.updateUIWhenStartingHTTPRequest(); OPTIONAL

        // Execute the stream subscribing to Observable defined inside ApiStreams
        // We call from the ApiStream class our Observable which will output the JSON data
        // retrieved from the Django API thanks to Retrofit. We subscribe to it by creating a
        // Subscriber (DisposableObserver) and placing the generated subscription in the
        // this.disposable class variable to avoid any Memory Leaks risk.


        this.disposable = ApiStreams.streamFetchCollectedProductsFollowing(
                "Token c5ebcab735b5c52ce2d01649fcfe8172c86b32c4",
                3)
                .subscribeWith(new DisposableObserver<List<Product>>() {
                    @Override
                    public void onNext(List<Product> products) {
                        // Update the GUI with list of products
                        // At the end of our stream, so once the list of products is uploaded,
                        // we will display it in our RecylerView. To do this, we add the downloaded
                        // items to our object list ( addAll(products) ) then we reload our Adapter
                        // thanks to its notifyDataSetChanged() method.
                        updateUI(products);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "On Error" + Log.getStackTraceString(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG", "On Complete !");
                    }
                });
    }
}
