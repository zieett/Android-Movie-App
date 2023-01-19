package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.movieapp.model.Movie;
import com.example.movieapp.model.OrderHistory;
import com.example.movieapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kotlin.UShort;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailTextView, passwordTextView,address,confirmPassword,name;
    private Button Btn;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String gender;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setTitle("Sign Up");
        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // initialising all views through id defined above
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.passwd);
        address = findViewById(R.id.signUpAddress);
        confirmPassword = findViewById(R.id.confirmPassword);
        name = findViewById(R.id.signUpName);
        Btn = findViewById(R.id.btnregister);
        progressbar = findViewById(R.id.progressbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Spinner dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"Male", "Female","Other"};
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

                Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                registerNewUser();
            }
        });
    }

    private void registerNewUser()
    {
        String email, password,confirmPass,addressString,myName;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
        confirmPass = confirmPassword.getText().toString();
        addressString = address.getText().toString();
        myName = name.getText().toString();
        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if(!confirmPass.equals(password)){
            Toast.makeText(getApplicationContext(),
                            "Your confirm password not correct!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if(myName.isEmpty()){
            Toast.makeText(getApplicationContext(),
                            "Please enter your name",
                            Toast.LENGTH_LONG)
                    .show();
        }
        progressbar.setVisibility(View.VISIBLE);
        // create new user or register new user
        mAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegistrationActivity.this,new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            User user = new User(email,new ArrayList<Movie>(),new ArrayList<Movie>(),myName,addressString,gender,new ArrayList<OrderHistory>());
                            db.collection("Users").document(uid).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegistrationActivity.this, "Fail to add user \n" + e, Toast.LENGTH_SHORT).show();
                                }
                            });
                            Toast.makeText(getApplicationContext(),
                                            "Registration successful!, Welcome" ,
                                            Toast.LENGTH_LONG)
                                    .show();
                            progressbar.setVisibility(View.GONE);
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                            // if the user created intent to login activity
                            finish();
                        }
                        else {
                            Toast.makeText(RegistrationActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_LONG).show();
                            Log.e("MyTag", task.getException().toString());
                            // Registration failed
                            // hide the progress bar
                            progressbar.setVisibility(View.GONE);
                        }
                    }
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