package com.example.cshare.Repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.cshare.Models.Product;
import com.example.cshare.Utils.ApiStreams;

import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class ProductRepository {

    private LiveData<List<Product>> allProducts;
}
