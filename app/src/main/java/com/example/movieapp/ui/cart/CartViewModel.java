package com.example.movieapp.ui.cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.model.Movie;

import java.util.List;

public class CartViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<List<Movie>> cart;

    public CartViewModel() {
        mText = new MutableLiveData<>();
        cart = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}