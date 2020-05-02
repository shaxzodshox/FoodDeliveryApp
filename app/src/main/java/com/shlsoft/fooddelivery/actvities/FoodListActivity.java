package com.shlsoft.fooddelivery.actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.shlsoft.fooddelivery.R;
import com.shlsoft.fooddelivery.app.BaseActivity;
import com.shlsoft.fooddelivery.interfaces.ItemClickListener;
import com.shlsoft.fooddelivery.model.Food;
import com.shlsoft.fooddelivery.view_holder.FoodViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodListActivity extends BaseActivity {

    private RecyclerView recycler_food;
    private RecyclerView.LayoutManager layoutManager;

    private String categoryId = "";

    DatabaseReference food_db_reference;

    FirebaseRecyclerOptions<Food> food_options;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> food_adapter;

    //Search Functionality
    FirebaseRecyclerAdapter<Food, FoodViewHolder> search_adapter;
    FirebaseRecyclerOptions<Food> search_option;
    List<String> suggest_list = new ArrayList<>();
    MaterialSearchBar searchBar;

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

        //Search
        searchBar = findViewById(R.id.searchbar);
        searchBar.setHint(getString(R.string.qidirish));
        loadSuggest(); //function to load suggestions from the firebase
        searchBar.setLastSuggestions(suggest_list);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //When user type their text then we will change suggest list
            List<String> suggest = new ArrayList<>();
            for(String search : suggest_list){//Loop in suggest list
                if(search.toLowerCase().contains(searchBar.getText().toLowerCase())){
                    suggest.add(search);
                }
                searchBar.setLastSuggestions(suggest);
            }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //When search bar is closed
                //Restore original adapter
                if(!enabled){
                        recycler_food.setAdapter(food_adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //When search finish
                //show result of search adapter
                startSearch(text);
                if(search_adapter != null) {
                    search_adapter.startListening();
                }
                recycler_food.setAdapter(search_adapter);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        search_option = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(food_db_reference.orderByChild("name").equalTo(text.toString()),Food.class) //Compare name
                .build();


        search_adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(search_option) {
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
                        foodDetail.putExtra("foodId",search_adapter.getRef(position).getKey());
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
    }

    private void loadSuggest() {
        food_db_reference.orderByChild("menuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Food item = snapshot.getValue(Food.class);
                        suggest_list.add(item.getName()); //Add name of food to suggest list
                    }
                    }

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
