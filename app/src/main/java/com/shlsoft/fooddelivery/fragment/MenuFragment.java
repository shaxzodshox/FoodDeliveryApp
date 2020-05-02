package com.shlsoft.fooddelivery.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shlsoft.fooddelivery.R;
import com.shlsoft.fooddelivery.actvities.CartActivity;
import com.shlsoft.fooddelivery.actvities.FoodListActivity;
import com.shlsoft.fooddelivery.interfaces.ItemClickListener;
import com.shlsoft.fooddelivery.model.Category;
import com.shlsoft.fooddelivery.view_holder.MenuViewHolder;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    public MenuFragment() {
        // Required empty public constructor
    }

    private RecyclerView recycler_menu;
    private RecyclerView.LayoutManager layoutManager;

    private FloatingActionButton fab_cart;

    private ProgressDialog progressDialog;

    DatabaseReference databaseReference;

    FirebaseRecyclerOptions<Category> category_options;
    FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab_cart = view.findViewById(R.id.fab_cart);

        recycler_menu = view.findViewById(R.id.recyclerview);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recycler_menu.setLayoutManager(layoutManager);

        showLoadingProgress();

        //InitFirebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Category");

        category_options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(databaseReference,Category.class).build();


        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(category_options) {
            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder menuViewHolder, int i, @NonNull Category category) {
                menuViewHolder.txtMenuName.setText(category.getName());
                Picasso.get()
                        .load(category.getImage())
                        .placeholder(R.drawable.food_placeholder)
                        .into(menuViewHolder.imgMenu);

                menuViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                       //Get categoryId and send it to new activity
                        Intent foodList = new Intent(getContext(), FoodListActivity.class);
                        //Because CategoryId is key,so we just get key of this item
                        foodList.putExtra("categoryId",adapter.getRef(position).getKey());
                        startActivity(foodList);
                    }
                });

                //Button click on FAB
                fab_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent cartIntent = new Intent(getContext(), CartActivity.class);
                        startActivity(cartIntent);
                    }
                });
            }

            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item,parent,false);
                return new MenuViewHolder(view);
            }
        };
        adapter.startListening();
        recycler_menu.setAdapter(adapter);

        //onDataChange called to remove progress bar..make a call to dataSnapshot.getChildrenCount() and based
        //on returned value hide progressbar

        databaseReference.addValueEventListener(new ValueEventListener() {
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
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.iltimos_kuting));
        progressDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter != null){
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if(adapter != null){
            adapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter != null){
            adapter.startListening();
        }
    }

}
