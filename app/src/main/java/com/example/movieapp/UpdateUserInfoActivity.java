package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.movieapp.databinding.ActivityUpdateUserInfoBinding;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class UpdateUserInfoActivity extends AppCompatActivity {
    ActivityUpdateUserInfoBinding binding;
    String gender;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateUserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        user = (User) bundle.get("user");
        Spinner dropdown = binding.spinner2;
        String[] items = new String[]{"Male", "Female", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dropdown.setSelection(getGenderPosition(user.getGender()));
        binding.updateUserName.setText(user.getName());
        binding.updateUserEmail.setText(user.getEmail());
        binding.updateUserAddress.setText(user.getAddress());
        binding.updateUserProgressBar.setVisibility(View.INVISIBLE);
        binding.confirmUpdateButton.setOnClickListener(e -> {
            updateData();
        });
    }

    private void updateData() {
        User newUser = new User(binding.updateUserEmail.getText().toString(),user.getWishlist(),user.getCart(),binding.updateUserName.getText().toString(),binding.updateUserAddress.getText().toString(),gender);
        FirebaseFirestore.getInstance().collection("Users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                .set(newUser)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                        Toast.makeText(UpdateUserInfoActivity.this, "Successfully update your information", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i("TAG", "Error getting documents.", task.getException());
                    }
                }
                );
    }


    private int getGenderPosition(String gender) {
        if (gender.equals("Male")) return 0;
        if (gender.equals("Female")) return 1;
        if (gender.equals("Other")) return 2;
        return 0;
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