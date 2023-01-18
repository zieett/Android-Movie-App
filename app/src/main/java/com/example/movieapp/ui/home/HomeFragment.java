package com.example.movieapp.ui.home;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.LoginActivity;
import com.example.movieapp.MovieDetailActivity;
import com.example.movieapp.R;
import com.example.movieapp.RegistrationActivity;
import com.example.movieapp.UserInformationActivity;
import com.example.movieapp.adapter.MoviesAdapter;
import com.example.movieapp.databinding.FragmentHomeBinding;
import com.example.movieapp.model.Movie;
import com.example.movieapp.viewmodel.UserViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private FirebaseAuth mAuth;
    private List<Movie> movieList = new ArrayList<>();
    private MoviesAdapter mAdapter;
    private FirebaseFirestore db;
//    private MenuItem menuItem;
//    private SearchView searchView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.homeProgressBar.setVisibility(View.VISIBLE);
        binding.popularMovieText.setVisibility(View.INVISIBLE);
        binding.whatToWatchText.setVisibility(View.INVISIBLE);
//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        mAuth = FirebaseAuth.getInstance();
        binding.accountIcon.setOnClickListener(e->{
            checkLogin();
        });
        db = FirebaseFirestore.getInstance();
        RecyclerView recyclerView = binding.recyclerView;
        mAdapter = new MoviesAdapter(getActivity(),movieList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        binding.popularMovieImage2.setOnClickListener(e ->{
            Intent intent = new Intent(getActivity(),MovieDetailActivity.class);
            db.collection("Movies").limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null) {
                            List<Movie> movies = task.getResult().toObjects(Movie.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("movie",movies.get(0));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                    else {
                        Log.i("TAG", "Error getting documents.", task.getException());
                    }
                }
            });
        });
        prepareMovieData();
        return root;
    }

    private void prepareMovieData() {
        db.collection("Movies")
                .orderBy("vote_average", Query.Direction.DESCENDING)
                .limit(6)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                List<Movie> movies = task.getResult().toObjects(Movie.class);
                                Picasso.get().load("https://image.tmdb.org/t/p/w200" + movies.get(0).poster_path)
                                        .fit()
                                        .into(binding.popularMovieImage2);
                                Picasso.get().load("https://image.tmdb.org/t/p/original" + movies.get(0).backdrop_path)
                                        .fit()
                                        .into(binding.popularMovieImage);
                                binding.popularMovieTitle.setText(movies.get(0).getTitle() + "\n" + "new trailer");
                                System.out.println(movies.size());
                                for (int i = 1; i< movies.size();i++){
                                    movieList.add(movies.get(i));
                                }
                                binding.homeProgressBar.setVisibility(View.INVISIBLE);
                                binding.whatToWatchText.setVisibility(View.VISIBLE);
                            }
                        }
                        else {
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
        movieList = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    public void checkLogin(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            Intent intent = new Intent(getActivity(), UserInformationActivity.class);
            startActivity(intent);
            System.out.println("User: " + user);
        }
        else{
            Intent intent = new Intent(getActivity(),LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mAdapter != null){
            mAdapter.release();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

}
