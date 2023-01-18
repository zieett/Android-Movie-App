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
import com.example.movieapp.ui.movies.MoviesFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class FireStoreMovieAdapter extends FirestoreRecyclerAdapter<Movie, FireStoreMovieAdapter.MovieViewHolder> {
    private Context mContext;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FireStoreMovieAdapter(@NonNull FirestoreRecyclerOptions<Movie> options,Context context) {
        super(options);
        this.mContext = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull MovieViewHolder holder, int position, @NonNull Movie model) {
//        Movie movie = moviesList.get(position);
        DecimalFormat df = new DecimalFormat("0.0");
        holder.movieTitle.setText(model.getTitle());
        holder.movieInfo.setText(df.format(model.getVote_average()) + " ");
        holder.movieDateAndTime.setText(model.getRelease_date() + " " + model.getTime());
        Picasso.get().load("https://image.tmdb.org/t/p/w200" + model.poster_path).fit().into(holder.movieImage);
        holder.layout_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMovieDetail(model);
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

    public void release(){
        mContext = null;
    }
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_layout_2, parent, false);
        return new MovieViewHolder(itemView);
    }
    class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView movieTitle,movieInfo,movieDateAndTime;
        ImageView movieImage;
        RelativeLayout layout_movie;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_movie = itemView.findViewById(R.id.layout_movie2);
            movieTitle = itemView.findViewById(R.id.movieLayout2Title);
            movieImage = itemView.findViewById(R.id.movieLayout2Image);
            movieInfo = itemView.findViewById(R.id.movieLayout2Info);
            movieDateAndTime = itemView.findViewById(R.id.movieLayout2DateAndTIme);
        }
    }
}
