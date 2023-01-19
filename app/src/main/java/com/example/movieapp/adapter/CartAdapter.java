package com.example.movieapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.MovieDetailActivity;
import com.example.movieapp.R;
import com.example.movieapp.model.Movie;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> implements View.OnClickListener {
    private List<Movie> moviesList;
    private Context mContext;
    private HandleItemClick clickListener;
    private boolean isOrderDetail;
    @Override
    public void onClick(View v) {

    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cartMovieTitle,cartMoviePrice;
        ImageView movieImage,deleteButton;
        RelativeLayout layout_movie;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_movie = itemView.findViewById(R.id.layout_movie3);
            cartMovieTitle = itemView.findViewById(R.id.cartMovieTitle);
            movieImage = itemView.findViewById(R.id.cartMovieImage);
            cartMoviePrice = itemView.findViewById(R.id.cartMoviePrice);
            deleteButton = itemView.findViewById(R.id.cartMovieDeleteButton);
        }
    }
    public CartAdapter(Context context,List<Movie> moviesList,HandleItemClick clickListener) {
        this.moviesList = moviesList;
        this.mContext = context;
        this.clickListener = clickListener;
        this.isOrderDetail = false;
    }
    public CartAdapter(List<Movie> moviesList){
        this.moviesList = moviesList;
        isOrderDetail = true;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_cart_layout, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DecimalFormat df = new DecimalFormat("0.0");
        Movie movie = moviesList.get(position);
        holder.cartMovieTitle.setText(movie.getTitle());
        holder.cartMoviePrice.setText(movie.getPrice() + " $");
        Picasso.get().load("https://image.tmdb.org/t/p/w200" + movie.poster_path).fit().into(holder.movieImage);
        if(isOrderDetail) {
            holder.deleteButton.setVisibility(View.INVISIBLE);
            return;
        }
        holder.deleteButton.setVisibility(View.VISIBLE);
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.itemClick(movie,position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void release(){
        mContext = null;
    }
    public interface  HandleItemClick {
        void itemClick(Movie movie,int position);
        void removeItem(Movie movie);
        void editItem(Movie movie);
    }

}
