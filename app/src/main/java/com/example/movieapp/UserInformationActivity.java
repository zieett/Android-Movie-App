package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.movieapp.databinding.ActivityMainBinding;
import com.example.movieapp.databinding.ActivityUserInformationBinding;
import com.example.movieapp.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class UserInformationActivity extends AppCompatActivity {
    private ActivityUserInformationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("User Information");
        binding.signOut.setOnClickListener(e ->{
            FirebaseAuth.getInstance().signOut();
            finish();
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}