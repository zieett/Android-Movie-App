package com.example.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.MovieDetailActivity;
import com.example.movieapp.R;
import com.example.movieapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> implements View.OnClickListener {
    private List<Movie> moviesList;
    private Context mContext;
    @Override
    public void onClick(View v) {

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView movieTitle,movieScore;
        ImageView movieImage;
        RelativeLayout layout_movie;
        MyViewHolder(View view) {
            super(view);
            layout_movie = view.findViewById(R.id.layout_movie);
            movieTitle = view.findViewById(R.id.movieTitle);
            movieImage = view.findViewById(R.id.movieImage);
            movieScore = view.findViewById(R.id.movieScore);
        }
    }
    public MoviesAdapter(Context context,List<Movie> moviesList) {
        this.moviesList = moviesList;
        this.mContext = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movies_list, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DecimalFormat df = new DecimalFormat("0.0");
        Movie movie = moviesList.get(position);
        holder.movieTitle.setText(movie.getTitle());
        holder.movieScore.setText(df.format(movie.getVote_average()));
        Picasso.get().load("https://image.tmdb.org/t/p/w200" + movie.poster_path).fit().into(holder.movieImage);
        holder.layout_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMovieDetail(movie);
            }
        });
    }
    private void goToMovieDetail(Movie movie){
        Intent intent = new Intent(mContext, MovieDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("movie",movie);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void release(){
        mContext = null;
    }

}