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
import com.example.movieapp.model.Cast;
import com.example.movieapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;
public class CastAdapter extends RecyclerView.Adapter<CastAdapter.MyViewHolder> implements View.OnClickListener {
    private List<Cast> castList;
    private Context mContext;
    @Override
    public void onClick(View v) {

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView castName,castMovieName;
        ImageView castImage;
        RelativeLayout layout_cast;
        MyViewHolder(View view) {
            super(view);
            layout_cast = view.findViewById(R.id.layout_cast);
            castName = view.findViewById(R.id.castName);
            castImage = view.findViewById(R.id.castImage);
            castMovieName = view.findViewById(R.id.castMovieName);
        }
    }
    public CastAdapter(Context context,List<Cast> castList) {
        this.castList = castList;
        this.mContext = context;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cast_list, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Cast cast = castList.get(position);
        holder.castName.setText(cast.getName());
        holder.castMovieName.setText(cast.getCharacter());
        Picasso.get().load("https://image.tmdb.org/t/p/w200" + cast.profile_path).fit().into(holder.castImage);
        holder.layout_cast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoCastDetail(cast);
            }
        });
    }
    private void gotoCastDetail(Cast cast){
//        Intent intent = new Intent(mContext, MovieDetailActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("cast",cast);
//        intent.putExtras(bundle);
//        mContext.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        return castList.size();
    }

    public void release(){
        mContext = null;
    }

}
