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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.MovieDetailActivity;
import com.example.movieapp.R;
import com.example.movieapp.model.Movie;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class FireStoreMovieAdapter extends FirestoreRecyclerAdapter<Movie, FireStoreMovieAdapter.MovieViewHolder> {
    private Context mContext;
    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FireStoreMovieAdapter(@NonNull FirestoreRecyclerOptions<Movie> options,Context context) {
        super(options);
        this.mContext = context;
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }
    @Override
    protected void onBindViewHolder(@NonNull MovieViewHolder holder, int position, @NonNull Movie model) {
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
        if(user.getEmail().equals("admin@gmail.com")){
            holder.deleteButton.setOnClickListener(v -> deleteMovie(position));
            return;
        }
        holder.deleteButton.setVisibility(ImageView.INVISIBLE);
    }
    private AlertDialog deleteMovie(int position){
        return new AlertDialog.Builder(mContext)
                .setMessage("Are you sure, you want to delete this movie?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getSnapshots().getSnapshot(position).getReference().delete();
                            }
                        }
                ).setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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
        ImageView movieImage,deleteButton;
        RelativeLayout layout_movie;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_movie = itemView.findViewById(R.id.layout_movie3);
            movieTitle = itemView.findViewById(R.id.movieLayout2Title);
            movieImage = itemView.findViewById(R.id.movieLayout2Image);
            movieInfo = itemView.findViewById(R.id.movieLayout2Info);
            movieDateAndTime = itemView.findViewById(R.id.movieLayout2DateAndTIme);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
