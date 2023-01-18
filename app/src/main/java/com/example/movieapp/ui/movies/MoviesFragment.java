package com.example.movieapp.ui.movies;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;
import com.example.movieapp.adapter.FireStoreMovieAdapter;
import com.example.movieapp.databinding.FragmentMoviesBinding;
import com.example.movieapp.model.Movie;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class MoviesFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private FragmentMoviesBinding binding;
    private FirestoreRecyclerAdapter adapter;
    private MenuItem menuItem;
    private SearchView searchView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMoviesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        db = FirebaseFirestore.getInstance();
        Query quey  = db.collection("Movies");
        FirestoreRecyclerOptions<Movie> options = new FirestoreRecyclerOptions.Builder<Movie>()
                .setQuery(quey.orderBy("original_title"),Movie.class).build();
        adapter = new FireStoreMovieAdapter(options,getActivity());
//        adapter = new FirestoreRecyclerAdapter<Movie,MovieViewHolder>(options){
//            @Override
//            protected void onBindViewHolder(@NonNull MovieViewHolder holder, int position, @NonNull Movie model) {
////                Movie movie = moviesList.get(position);
//                holder.movieTitle.setText(model.getTitle());
//                holder.movieScore.setText(String.valueOf(model.getVote_average()));
//                Picasso.get().load("https://image.tmdb.org/t/p/w200" + model.poster_path).fit().into(holder.movieImage);
//            }
//
//            @NonNull
//            @Override
//            public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View itemView = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.movies_list, parent, false);
//                return new MovieViewHolder(itemView);
//            }
//        };
        recyclerView = binding.moviesLayout;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu,menu);
        menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setIconified(true);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mySearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mySearch(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void mySearch(String query) {
        FirestoreRecyclerOptions<Movie> options = new FirestoreRecyclerOptions.Builder<Movie>()
                .setQuery(db.collection("Movies").orderBy("original_title").startAt(query).endAt(query + "\uf8ff"),Movie.class).build();
        adapter = new FireStoreMovieAdapter(options,getActivity());
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
