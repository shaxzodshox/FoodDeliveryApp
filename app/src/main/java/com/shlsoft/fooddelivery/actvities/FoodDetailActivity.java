package com.shlsoft.fooddelivery.actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shlsoft.fooddelivery.R;
import com.shlsoft.fooddelivery.model.Food;
import com.squareup.picasso.Picasso;

public class FoodDetailActivity extends AppCompatActivity {

    private TextView food_name, food_price, food_description;
    private ImageView food_image;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton fab_cart;
    private ElegantNumberButton numberButton;

    String food_id = "";

    FirebaseDatabase database;
    DatabaseReference foods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        //Init views
        initViews();
    }

    private void initViews() {
        food_name = findViewById(R.id.food_name);
        food_image = findViewById(R.id.img_food);
        food_price = findViewById(R.id.food_price);
        food_description = findViewById(R.id.food_description);

        fab_cart = findViewById(R.id.fab_btn_cart);
        numberButton = findViewById(R.id.elegant_btn);

        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        //Get FoodId from Intent

        if(getIntent() != null){
            food_id = getIntent().getStringExtra("foodId");

            if(!food_id.isEmpty()){
                getDetailFood(food_id);
            }

        }
    }

    private void getDetailFood(final String food_id) {
        foods.child(food_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Food food = dataSnapshot.getValue(Food.class);

                //SetImage
                Picasso.get().load(food.getImage()).into(food_image);

                collapsingToolbarLayout.setTitle(food.getName());

                food_price.setText(food.getPrice());
                food_name.setText(food.getName());
                food_description.setText(food.getDescription());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
