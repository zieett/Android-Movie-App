package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.movieapp.databinding.ActivityCheckoutBinding;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.OrderHistory;
import com.example.movieapp.model.User;
import com.example.movieapp.ultil.RegexInputFilter;
import com.example.movieapp.ultil.InputFilterMinMax;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CheckoutActivity extends AppCompatActivity {
    private RadioButton radioCod, radioCard;
    RadioButton selectedRadioButton;
    RadioGroup radioGroup;
    private ActivityCheckoutBinding binding;
    EditText cardNumber;
    List<Movie> cart;
    int totalAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        radioGroup = binding.radioGroup;
        selectedRadioButton = findViewById(radioGroup.getCheckedRadioButtonId());
        cart = (List<Movie>) getIntent().getExtras().get("cart");
        totalAmount = (int) getIntent().getExtras().get("totalAmount");
        System.out.println(cart.toString());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedRadioButton = findViewById(checkedId);
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.paymentMethodLayout);
                cardNumber = new EditText(CheckoutActivity.this);
                RelativeLayout.LayoutParams mRparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                cardNumber.setLayoutParams(mRparams);
                cardNumber.setHint("Please enter your card number ");
                cardNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                cardNumber.setFilters(new InputFilter[]{new RegexInputFilter("[0-9]"), new InputFilterMinMax(0, 999999999)});
                if (!selectedRadioButton.getText().equals("COD")) {
                    linearLayout.addView(cardNumber);
                } else {
                    linearLayout.removeViewAt(linearLayout.getChildCount() - 1);
                }
            }
        });
        binding.checkoutPaymentButton.setOnClickListener(e -> {
            if (binding.checkoutAddressEditText.getText().toString().isEmpty()) {
                Toast.makeText(this, "You did not enter your address", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!selectedRadioButton.getText().equals("COD") && (cardNumber == null || cardNumber.getText().toString().isEmpty() || cardNumber.getText().toString().length() != 9)) {
                Toast.makeText(this, "You card number must be 9 digits", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(CheckoutActivity.this)
                    .setMessage("Are you sure to payment?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                                    OrderHistory orderHistory = new OrderHistory(cart,String.valueOf(totalAmount), LocalDateTime.now().format(df),"Shipping");
                                    FirebaseFirestore.getInstance().collection("Users")
                                            .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                            .update("orderHistory", FieldValue.arrayUnion(orderHistory),"cart",new ArrayList<>())
                                            .addOnCompleteListener(task -> {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(CheckoutActivity.this, "Payment successfully, check order history for shipment status, Thank you!", Toast.LENGTH_SHORT).show();
                                                            Intent returnIntent = new Intent();
                                                            setResult(Activity.RESULT_OK,returnIntent);
                                                            finish();
                                                        } else {
                                                            Log.i("TAG", "Error getting documents.", task.getException());
                                                        }
                                                    }
                                            );

                                    finish();
                                }
                            }
                    ).setNegativeButton("Cancel", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


        });
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

}