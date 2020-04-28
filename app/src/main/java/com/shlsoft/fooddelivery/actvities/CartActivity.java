package com.shlsoft.fooddelivery.actvities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shlsoft.fooddelivery.R;
import com.shlsoft.fooddelivery.adapter.CartAdapter;
import com.shlsoft.fooddelivery.db.Database;
import com.shlsoft.fooddelivery.model.Order;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.widget.FButton;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference request;

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
        request = database.getReference("Requests");

        //Init
        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        tv_total_price = findViewById(R.id.tv_total);
        btn_place_order = findViewById(R.id.btnPlaceOrder);

        loadListFood();
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
