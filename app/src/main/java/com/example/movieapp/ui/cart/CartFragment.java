package com.example.movieapp.ui.cart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.adapter.WishlistAdapter;
import com.example.movieapp.databinding.FragmentCartBinding;
import com.example.movieapp.databinding.FragmentWishlistBinding;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {

    private FragmentCartBinding binding;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private WishlistAdapter mAdapter;
    private MenuItem menuItem;
    private SearchView searchView;
    private List<Movie> cart = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CartViewModel cartViewModel =
                new ViewModelProvider(this).get(CartViewModel.class);

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {
            binding.cartTextView.setVisibility(View.INVISIBLE);
            db = FirebaseFirestore.getInstance();
            RecyclerView recyclerView = binding.cartLayout;
            mAdapter = new WishlistAdapter(getActivity(), cart);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            prepareCartList();
        } else {
            binding.cartTextView.setText("Please login to view your wishlist");
        }

        return root;
    }

    private void prepareCartList() {
        db.collection("Users")
                .document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   if (task.getResult() != null) {
                                                       User user = task.getResult().toObject(User.class);
                                                       cart.addAll(user.getCart());
                                                       if(cart.size() == 0) {
                                                           binding.cartTextView.setVisibility(View.VISIBLE);
                                                           binding.cartTextView.setText("Your cart is empty");
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}