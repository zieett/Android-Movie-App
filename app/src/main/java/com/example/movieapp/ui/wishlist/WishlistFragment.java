package com.example.movieapp.ui.wishlist;

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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.adapter.FireStoreMovieAdapter;
import com.example.movieapp.adapter.MoviesAdapter;
import com.example.movieapp.adapter.WishlistAdapter;
import com.example.movieapp.databinding.FragmentMoviesBinding;
import com.example.movieapp.databinding.FragmentWishlistBinding;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.User;
import com.example.movieapp.viewmodel.UserViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WishlistFragment extends Fragment {

    private FragmentWishlistBinding binding;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private WishlistAdapter mAdapter;
    private MenuItem menuItem;
    private SearchView searchView;
    private List<Movie> movieList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWishlistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {
            binding.wishlistText.setVisibility(View.INVISIBLE);
            db = FirebaseFirestore.getInstance();
            RecyclerView recyclerView = binding.wishlistLayout;
            mAdapter = new WishlistAdapter(getActivity(), movieList);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            prepareMovieWishList();
        } else {
            binding.wishlistText.setText("Please login to view your wishlist");
        }
        return root;
    }

    private void prepareMovieWishList() {
        db.collection("Users")
                .document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   if (task.getResult() != null) {
                                                       User user = task.getResult().toObject(User.class);
                                                       binding.wishlistProgressBar.setVisibility(View.INVISIBLE);
                                                       for (int i = 0; i < user.getWishlist().size(); i++) {
                                                           movieList.add(user.getWishlist().get(i));
                                                       }
                                                       if(movieList.size() == 0) {
                                                           binding.wishlistText.setVisibility(View.VISIBLE);
                                                           binding.wishlistText.setText("Your wishlist is empty");
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

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onStart() {
        super.onStart();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mAdapter != null){
            mAdapter.release();
        }
    }
}