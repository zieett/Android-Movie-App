package com.example.movieapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.MovieDetailActivity;
import com.example.movieapp.OrderHistoryActivity;
import com.example.movieapp.OrderHistoryDetailActivity;
import com.example.movieapp.R;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.OrderHistory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> implements View.OnClickListener {
    private List<OrderHistory> orderHistories;
    private Context mContext;
    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore;
    @Override
    public void onClick(View v) {

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderNum,orderDate,orderStatus;
        LinearLayout layout_movie;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_movie = itemView.findViewById(R.id.layout_movie_4);
            orderNum = itemView.findViewById(R.id.orderNum);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderStatus = itemView.findViewById(R.id.orderStatus);

        }
    }
    public OrderHistoryAdapter(Context context,List<OrderHistory> orderHistories) {
        this.orderHistories = orderHistories;
        this.mContext = context;
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_history_layout, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DecimalFormat df = new DecimalFormat("0.0");
        OrderHistory orderHistory = orderHistories.get(position);
        holder.orderDate.setText(orderHistory.getDate());
        holder.orderNum.setText("Order #" + (position+1));
        holder.orderStatus.setText("Status: " + orderHistory.getStatus());
        holder.layout_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToOrderDetail(orderHistory);
            }
        });
    }
    private void goToOrderDetail(OrderHistory orderHistory){
        Intent intent = new Intent(mContext, OrderHistoryDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("orderHistory",orderHistory);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        return orderHistories.size();
    }

    public void release(){
        mContext = null;
    }

}
