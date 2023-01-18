package com.example.movieapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.model.User;

public class UserViewModel extends ViewModel {
    private final MutableLiveData<User> user;
    private final MutableLiveData<Boolean> isLogin;
     public MutableLiveData<String> test = new MutableLiveData<>();

    public UserViewModel() {
        isLogin = new MutableLiveData<>();
        user = new MutableLiveData<User>();
    }

    public MutableLiveData<User> getUser() {
        return user;
    }
    public MutableLiveData<Boolean> getIsLogin(){return isLogin;}
    public MutableLiveData<String> getTest() {
        return test;
    }

}
