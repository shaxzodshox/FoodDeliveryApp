package com.shlsoft.fooddelivery.actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shlsoft.fooddelivery.R;
import com.shlsoft.fooddelivery.app.BaseActivity;
import com.shlsoft.fooddelivery.interfaces.ItemClickListener;
import com.shlsoft.fooddelivery.model.Food;
import com.shlsoft.fooddelivery.view_holder.FoodViewHolder;
import com.squareup.picasso.Picasso;

public class FoodListActivity extends BaseActivity {

    private RecyclerView recycler_food;
    private RecyclerView.LayoutManager layoutManager;

    private String categoryId = "";

    DatabaseReference food_db_reference;

    FirebaseRecyclerOptions<Food> food_options;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> food_adapter;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        recycler_food = findViewById(R.id.recyclerview);
        recycler_food.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_food.setLayoutManager(layoutManager);

        showLoadingProgress();

        if(getIntent() != null){
            categoryId = getIntent().getStringExtra("categoryId");
        }

        //InitFirebase
        food_db_reference = FirebaseDatabase.getInstance().getReference("Foods");

        food_options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(food_db_reference.orderByChild("menuId").equalTo(categoryId),Food.class)
                .build();

        food_adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(food_options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int i, @NonNull Food food) {
                foodViewHolder.txtFoodName.setText(food.getName());
                Picasso.get()
                        .load(food.getImage())
                        .placeholder(R.drawable.food_placeholder)
                        .into(foodViewHolder.imgFood);

                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start new activity
                        Intent foodDetail = new Intent(FoodListActivity.this, FoodDetailActivity.class);
                        foodDetail.putExtra("foodId",food_adapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });

            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false);
                return new FoodViewHolder(view);
            }
        };
        food_adapter.startListening();
        recycler_food.setAdapter(food_adapter);

        food_db_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    if(progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                }
                else {
                    /* If no data found  */
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    },2000);}}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showLoadingProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.iltimos_kuting));
        progressDialog.show();
    }


    @Override
    public void onStart() {
        super.onStart();
        if(food_adapter != null){
            food_adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if(food_adapter != null){
            food_adapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(food_adapter != null){
            food_adapter.startListening();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }
}
