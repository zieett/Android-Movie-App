package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;


import com.example.movieapp.databinding.ActivityUserInformationBinding;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class UserInformationActivity extends AppCompatActivity {
    private ActivityUserInformationBinding binding;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("User Information");
        binding.signOut.setVisibility(View.INVISIBLE);
        binding.myCardView.setVisibility(View.INVISIBLE);
        binding.userNameText.setVisibility(View.INVISIBLE);
        binding.userAddressText.setVisibility(View.INVISIBLE);
        binding.userEmailText.setVisibility(View.INVISIBLE);
        binding.userGenderText.setVisibility(View.INVISIBLE);
        Picasso.get().load("https://img.freepik.com/free-vector/film-strip-background-with-clapper-board_1017-33456.jpg?w=2000").fit().into(binding.userBackgroundImage);
        Picasso.get().load("https://t3.ftcdn.net/jpg/01/18/01/98/360_F_118019822_6CKXP6rXmVhDOzbXZlLqEM2ya4HhYzSV.jpg").fit().into(binding.userImage);
        binding.signOut.setOnClickListener(e -> {
            FirebaseAuth.getInstance().signOut();
            finish();
        });
        binding.updateUserButton.setOnClickListener(e ->{
            Intent intent = new Intent(this,UpdateUserInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user",user);
            intent.putExtras(bundle);
            startActivityForResult(intent,1);
        });
        prepareUserData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            prepareUserData();
        }
    }

    private void prepareUserData() {
        FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   if (task.getResult() != null) {
                                                       user = task.getResult().toObject(User.class);
                                                       binding.userName.setText(user.getName());
                                                       binding.userEmail.setText(user.getAddress());
                                                       binding.userEmail.setText(user.getEmail());
                                                       binding.userGender.setText(user.getGender());
                                                       binding.userDetailProgressBar.setVisibility(View.INVISIBLE);
                                                       binding.signOut.setVisibility(View.VISIBLE);
                                                       binding.myCardView.setVisibility(View.VISIBLE);
                                                       binding.userNameText.setVisibility(View.VISIBLE);
                                                       binding.userAddressText.setVisibility(View.VISIBLE);
                                                       binding.userEmailText.setVisibility(View.VISIBLE);
                                                       binding.userGenderText.setVisibility(View.VISIBLE);
                                                   }
                                               } else {
                                                   Log.i("TAG", "Error getting documents.", task.getException());
                                               }
                                           }
                                       }
                );
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