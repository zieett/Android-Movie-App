package com.example.movieapp.ui.cart;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.CheckoutActivity;
import com.example.movieapp.MainActivity;
import com.example.movieapp.adapter.CartAdapter;
import com.example.movieapp.adapter.WishlistAdapter;
import com.example.movieapp.databinding.FragmentCartBinding;
import com.example.movieapp.databinding.FragmentWishlistBinding;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment implements CartAdapter.HandleItemClick{

    private FragmentCartBinding binding;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private CartAdapter mAdapter;
    private CartViewModel cartViewModel;
    private MenuItem menuItem;
    private SearchView searchView;
    private int totalPrice;
    private List<Movie> cart = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        binding.cartProgressBar.setVisibility(View.VISIBLE);
        binding.cartCheckOutText.setVisibility(View.INVISIBLE);
        binding.cartTotalText.setVisibility(View.INVISIBLE);
        binding.cartBuyButton.setVisibility(View.INVISIBLE);
        binding.cartTotal.setVisibility(View.INVISIBLE);
        binding.cartNumItem.setVisibility(View.INVISIBLE);
        if (user != null) {
            db = FirebaseFirestore.getInstance();
            recyclerView = binding.cartLayout;
            mAdapter = new CartAdapter(getActivity(), cart,this);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            prepareCartList();
            binding.cartBuyButton.setOnClickListener(e ->{
                Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("cart", (Serializable) cart);
                bundle.putInt("totalAmount",totalPrice);
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
            });
        } else {
            binding.cartTextView.setText("Please login to view your cart");
            binding.cartCheckOutText.setVisibility(View.INVISIBLE);
            binding.cartTotalText.setVisibility(View.INVISIBLE);
            binding.cartBuyButton.setVisibility(View.INVISIBLE);
            binding.cartTotal.setVisibility(View.INVISIBLE);
            binding.cartNumItem.setVisibility(View.INVISIBLE);
        }
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.cartLayout.setVisibility(View.INVISIBLE);
        binding.cartProgressBar.setVisibility(View.VISIBLE);
        binding.cartCheckOutText.setVisibility(View.INVISIBLE);
        binding.cartTotalText.setVisibility(View.INVISIBLE);
        binding.cartBuyButton.setVisibility(View.INVISIBLE);
        binding.cartTotal.setVisibility(View.INVISIBLE);
        binding.cartNumItem.setVisibility(View.INVISIBLE);
        prepareCartList();
    }

    private void prepareCartList() {
        db.collection("Users")
                .document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   if (task.getResult() != null) {
                                                       User user = task.getResult().toObject(User.class);
                                                       cart.clear();
                                                       cart.addAll(user.getCart());
                                                       System.out.println(cart.toString());
                                                       if(cart.size() > 0) {
                                                           binding.cartLayout.setVisibility(View.VISIBLE);
                                                           binding.cartTextView.setVisibility(View.INVISIBLE);
                                                           binding.cartCheckOutText.setVisibility(View.VISIBLE);
                                                           binding.cartTotalText.setVisibility(View.VISIBLE);
                                                           binding.cartBuyButton.setVisibility(View.VISIBLE);
                                                           binding.cartTotal.setVisibility(View.VISIBLE);
                                                           binding.cartNumItem.setVisibility(View.VISIBLE);
                                                       }
                                                       else{
                                                           binding.cartTextView.setVisibility(View.VISIBLE);
                                                           binding.cartTextView.setText("Your cart is empty");
                                                           binding.cartCheckOutText.setVisibility(View.INVISIBLE);
                                                           binding.cartTotalText.setVisibility(View.INVISIBLE);
                                                           binding.cartBuyButton.setVisibility(View.INVISIBLE);
                                                           binding.cartTotal.setVisibility(View.INVISIBLE);
                                                           binding.cartNumItem.setVisibility(View.INVISIBLE);
                                                       }
                                                       binding.cartNumItem.setText(cart.size() + " movie(s)");
                                                       totalPrice = 0;
                                                       for(Movie movie: cart){
                                                           totalPrice+= Integer.parseInt(movie.getPrice());
                                                       }
                                                       binding.cartTotal.setText(totalPrice + " $");
                                                       binding.cartProgressBar.setVisibility(View.INVISIBLE);
                                                       mAdapter.notifyDataSetChanged();
                                                   }
                                               } else {
                                                   Log.i("TAG", "Error getting documents.", task.getException());
                                               }

                                           }
                                       }
                );
    }



    public void delete(int position){
        cart.remove(position);
        new AlertDialog.Builder(getActivity())
                .setMessage("Are you sure to remove this movie from your cart?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseFirestore.getInstance().collection("Users")
                                        .document(user.getUid()).update("cart",cart).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getActivity(), "Remove Successfully", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getActivity(), "Failed to delete", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                ).setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void itemClick(Movie movie,int position) {
        cart.remove(position);
        new AlertDialog.Builder(getActivity())
                .setMessage("Are you sure to remove this movie from your cart?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseFirestore.getInstance().collection("Users")
                                        .document(user.getUid()).update("cart",cart).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    prepareCartList();
                                                    Toast.makeText(getActivity(), "Remove Successfully", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getActivity(), "Failed to delete", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                ).setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void removeItem(Movie movie) {

    }

    @Override
    public void editItem(Movie movie) {

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mAdapter != null){
            mAdapter.release();
        }
    }
}