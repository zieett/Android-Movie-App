package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.movieapp.adapter.OrderHistoryAdapter;
import com.example.movieapp.databinding.ActivityOrderHistoryBinding;
import com.example.movieapp.model.OrderHistory;
import com.example.movieapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderHistoryActivity extends AppCompatActivity {
    private ActivityOrderHistoryBinding binding;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private OrderHistoryAdapter mAdapter;
    private List<OrderHistory> orderHistories = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("Order History");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        recyclerView = binding.orderHistoryLayout;
        mAdapter = new OrderHistoryAdapter(this, orderHistories);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        prepareOrderList();
    }

    private void prepareOrderList() {
        db.collection("Users")
                .document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   if (task.getResult() != null) {
                                                       User user = task.getResult().toObject(User.class);
                                                       for (int i = 0; i < user.getOrderHistory().size(); i++) {
                                                           orderHistories.add(user.getOrderHistory().get(i));
                                                       }
                                                       if(orderHistories.size() == 0) {
//                                                           binding.wishlistText.setVisibility(View.VISIBLE);
//                                                           binding.wishlistText.setText("Your wishlist is empty");
                                                       }
                                                   }
                                               } else {
                                                   Log.i("TAG", "Error getting documents.", task.getException());
                                               }
                                               mAdapter.notifyDataSetChanged();
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














