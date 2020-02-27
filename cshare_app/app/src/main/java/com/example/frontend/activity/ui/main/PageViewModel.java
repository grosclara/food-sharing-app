package com.example.frontend.activity.ui.main;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    private MutableLiveData<Integer> mutableLiveDataIndex = new MutableLiveData<>();

    public void setIndex(int index) {
        mutableLiveDataIndex.setValue(index);
    }
}