package com.shlsoft.fooddelivery.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.shlsoft.fooddelivery.common.Common;
import com.shlsoft.fooddelivery.model.Request;
import com.shlsoft.fooddelivery.view_holder.OrderViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {

    public OrderFragment() {
        // Required empty public constructor
    }

    public RecyclerView rv_order_status;
    public RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference requests;

    private FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;
    private FirebaseRecyclerOptions<Request> recyclerOptions;

    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        rv_order_status = view.findViewById(R.id.rv_order_status);
        rv_order_status.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        rv_order_status.setLayoutManager(layoutManager);

        showLoadingProgress();

        loadOrders(Common.current_user.getPhone());
    }

    private void loadOrders(String phone) {
        recyclerOptions = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(requests.orderByChild("phone").equalTo(phone), Request.class).build();

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i, @NonNull Request request) {
                orderViewHolder.tv_order_id.setText(getString(R.string.buyurtma_id) + adapter.getRef(i).getKey());
                orderViewHolder.tv_order_status.setText(convertCodeToStatus(request.getStatus()));
                orderViewHolder.tv_order_address.setText(request.getAddress());
                orderViewHolder.tv_order_phone.setText(request.getPhone());
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false);
                return new OrderViewHolder(view);
            }
        };

        adapter.startListening();
        rv_order_status.setAdapter(adapter);

        requests.addValueEventListener(new ValueEventListener() {
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

    private String convertCodeToStatus(String status) {
        if (status.equals("0"))
            return "Buyurtma tayyorlanmoqda";
        else if (status.equals("1"))
            return "Buyurtma yetkazilmoqda";
        else
            return "Buyurtma yetkazildi";
    }

}
