package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.movieapp.adapter.CastAdapter;
import com.example.movieapp.adapter.MoviesAdapter;
import com.example.movieapp.databinding.ActivityMovieDetailBinding;
import com.example.movieapp.model.Cast;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.A;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MovieDetailActivity extends YouTubeBaseActivity {
    private ActivityMovieDetailBinding binding;
    private CastAdapter mAdapter;
    private ArrayList<Cast> castList = new ArrayList<>();
    private RatingBar ratingBar;
    private FirebaseFirestore firebaseFirestore;
    private Movie movie;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private static final DecimalFormat df = new DecimalFormat("0.0");
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private String api_key = "AIzaSyCoUk77AN_K-TRxrtj7GHGDNUzN1b9sV94";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        YouTubePlayerView ytPlayer = (YouTubePlayerView)findViewById(R.id.castBigImage);
        ytPlayer.initialize(
                api_key,
                new YouTubePlayer.OnInitializedListener() {
                    // Implement two methods by clicking on red
                    // error bulb inside onInitializationSuccess
                    // method add the video link or the playlist
                    // link that you want to play In here we
                    // also handle the play and pause
                    // functionality
                    @Override
                    public void onInitializationSuccess(
                            YouTubePlayer.Provider provider,
                            YouTubePlayer youTubePlayer, boolean b)
                    {
                        youTubePlayer.loadVideo(movie.getVideo_id());
                        youTubePlayer.play();
                    }

                    // Inside onInitializationFailure
                    // implement the failure functionality
                    // Here we will show toast
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult
                                                                youTubeInitializationResult)
                    {
                        Toast.makeText(getApplicationContext(), "Video player Failed", Toast.LENGTH_SHORT).show();
                    }
                });
        binding.actionbartitle.setOnClickListener(e -> finish());
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        movie = (Movie) bundle.get("movie");
        ratingBar = binding.ratingBar;
//        VideoView videoView = findViewById(R.id.castBigImage); //id in your xml file
//        videoView.setVideoURI(Uri.parse("https://www.youtube.com/watch?v="+ movie.getVideo_id())); //the string of the URL mentioned above
//        videoView.start();
//        Picasso.get().load("https://image.tmdb.org/t/p/original" + movie.backdrop_path).fit().into(binding.castBigImage);
        binding.movieDetailTitle.setText(movie.getTitle());
        binding.movieDetailInfo.setText(movie.getRelease_date().split("-")[0] + " " + movie.getTime());
        Picasso.get().load("https://image.tmdb.org/t/p/w200" + movie.poster_path).fit().into(binding.movieDetailImage);
        binding.movieDetailGenre.setText(movie.getGenre_ids().toString());
        binding.movieDetailDescription.setText(movie.getOverview());
        binding.movieDetailScore.setText(df.format(movie.getVote_average()));
        ratingBar.setRating((float) movie.getVote_average());
        binding.addToWatchList.setOnClickListener(e -> {
            update("wishlist", "wistlist");
        });
        if (LocalDate.now().isBefore(LocalDate.parse(movie.getRelease_date(), dateTimeFormatter).plusMonths(7))) {
            binding.notAvaiableButtton.setVisibility(View.VISIBLE);
            binding.buyButton.setVisibility(View.INVISIBLE);
        } else {
            binding.notAvaiableButtton.setVisibility(View.INVISIBLE);
            binding.buyButton.setVisibility(View.VISIBLE);
        }

        binding.buyButton.setOnClickListener(e -> update("cart", "cart"));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                updateRating(rating);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
        RecyclerView recyclerView = binding.recyclerView2;
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new CastAdapter(this, castList);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        castList.addAll(movie.getCast());
        mAdapter.notifyDataSetChanged();

    }

    private void update(String field, String msg) {
        firebaseFirestore.collection("Users")
                .document(user.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            User newUser = task.getResult().toObject(User.class);
                            System.out.println(newUser.toString());
                            if (field.equals("wishlist")) {
                                for (Movie userMovie : newUser.getWishlist()) {
                                    if (userMovie.getOriginal_title().equals(movie.getOriginal_title())) {
                                        Toast.makeText(MovieDetailActivity.this, "This movie is already on your " + msg, Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            } else {
                                for (Movie userMovie : newUser.getCart()) {
                                    if (userMovie.getOriginal_title().equals(movie.getOriginal_title())) {
                                        Toast.makeText(MovieDetailActivity.this, "This movie is already on your " + msg, Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }
                            firebaseFirestore.collection("Users")
                                    .document(Objects.requireNonNull(user.getUid()))
                                    .update(field, FieldValue.arrayUnion(movie))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                               @Override
                                                               public void onComplete(@NonNull Task<Void> task) {
                                                                   if (task.isSuccessful()) {
                                                                       Toast.makeText(MovieDetailActivity.this, "Successfully add to your " + msg, Toast.LENGTH_SHORT).show();
                                                                   } else {
                                                                       Log.i("TAG", "Error getting documents.", task.getException());
                                                                   }
                                                               }
                                                           }
                                    );
                        }
                    }
                });
    }

    ;
//        firebaseFirestore.collection("User")
//                .whereEqualTo("email",user.getEmail())
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                           @Override
//                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                               if (task.isSuccessful()) {
//                                                   if (task.getResult() != null) {
//                                                       List<User> users = task.getResult().toObjects(User.class);
//                                                       User newUser = users.get(0);
//
//                                                   }
//                                               }
//                                               else {
//                                                   Log.i("TAG", "Error getting documents.", task.getException());
//                                               }
//                                               mAdapter.notifyDataSetChanged();
//                                           }
//                                       }
//                );


    private void updateRating(float rating) {
        movie.setVote_average((float) ((movie.getVote_count() * movie.getVote_average() + rating) / (movie.getVote_count() + 1)));
        movie.setVote_count(movie.getVote_count() + 1);
        firebaseFirestore.collection("Movies")
                .document(String.valueOf(movie.getId()))
                .set(movie)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        binding.movieDetailScore.setText(df.format(movie.getVote_average()));
                        binding.ratingBar.setRating(rating);
                        Toast.makeText(MovieDetailActivity.this, "Rating has been submited..", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    // inside on failure method we are
                    // displaying a failure message.
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MovieDetailActivity.this, "Fail to submit the rating..", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void prepareCastData() {

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

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.release();
        }
    }
}