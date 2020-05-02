package com.shlsoft.fooddelivery.actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shlsoft.fooddelivery.R;
import com.shlsoft.fooddelivery.app.BaseActivity;
import com.shlsoft.fooddelivery.common.Common;
import com.shlsoft.fooddelivery.interfaces.ItemClickListener;
import com.shlsoft.fooddelivery.model.Request;
import com.shlsoft.fooddelivery.util.Toasts;
import com.shlsoft.fooddelivery.view_holder.OrderViewHolder;

import es.dmoral.toasty.Toasty;

public class OrderStatusActivity extends BaseActivity {

    public RecyclerView rv_order_status;
    public RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference requests;

    private FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;
    private FirebaseRecyclerOptions<Request> recyclerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        rv_order_status = findViewById(R.id.rv_order_status);
        rv_order_status.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rv_order_status.setLayoutManager(layoutManager);

        loadOrders(Common.current_user.getPhone());


    }

    private void loadOrders(String phone) {
        recyclerOptions = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(requests.orderByChild("phone").equalTo(phone), Request.class).build();

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i, @NonNull final Request request) {
                orderViewHolder.tv_order_id.setText(adapter.getRef(i).getKey());
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
    }

    private String convertCodeToStatus(String status) {
        if (status.equals("0"))
            return "Buyurtma tayyorlanmoqda";
        else if (status.equals("1"))
            return "Buyurtma yetkazilmoqda";
        else
            return "Buyurtma yetkazildi";
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

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }
}
