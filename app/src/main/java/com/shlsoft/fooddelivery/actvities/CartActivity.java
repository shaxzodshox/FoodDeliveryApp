package com.shlsoft.fooddelivery.actvities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shlsoft.fooddelivery.R;
import com.shlsoft.fooddelivery.adapter.CartAdapter;
import com.shlsoft.fooddelivery.app.BaseActivity;
import com.shlsoft.fooddelivery.common.Common;
import com.shlsoft.fooddelivery.db.Database;
import com.shlsoft.fooddelivery.model.Order;
import com.shlsoft.fooddelivery.model.Request;
import com.shlsoft.fooddelivery.util.Toasts;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.widget.FButton;

public class CartActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    private TextView tv_total_price;
    private FButton btn_place_order;

    List<Order> cart = new ArrayList<>();
    CartAdapter cartAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //Init
        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        tv_total_price = findViewById(R.id.tv_total);
        btn_place_order = findViewById(R.id.btnPlaceOrder);

        loadListFood();

        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create new Request
                showAlertDialog();
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.yana_bir_qadam).setMessage(R.string.manzil_kiriting);
        final EditText edtAddress = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtAddress.setLayoutParams(lp);
        builder.setView(edtAddress);
        builder.setIcon(R.drawable.ic_cart);
        builder.setPositiveButton(getString(R.string.buyurtma), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (edtAddress.getText().toString().isEmpty()) {
                    edtAddress.setError(getString(R.string.yetkazish_manzilini_kiriting));
                } else {
                    Request request = new Request(
                            Common.current_user.getPhone(),
                            Common.current_user.getName(),
                            edtAddress.getText().toString(),
                            tv_total_price.getText().toString(),
                            cart
                    );

                    //Submit to Firebase
                    //We will use System.CurrentMilli to key
                    requests.child(String.valueOf(System.currentTimeMillis()))
                            .setValue(request);

                    //Delete cart
                    new Database(getBaseContext()).cleanCart();
                    Toasts.showSuccessToast(getString(R.string.buyurtma_uchun_rahmat));
                    finish();
                }
            }
        });

        builder.setNegativeButton(getString(R.string.qaytish), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void loadListFood() {
        cart = new Database(this).getCarts();
        cartAdapter = new CartAdapter(cart, this);
        recyclerView.setAdapter(cartAdapter);

        //Calculate the total price
        long total = 0;
        for (Order order : cart) {
            total += (Long.parseLong(order.getPrice())) * (Long.parseLong(order.getQuantity()));
            tv_total_price.setText(total + " sum");
        }
    }
}
