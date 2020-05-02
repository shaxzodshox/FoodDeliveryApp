package com.shlsoft.fooddelivery.view_holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shlsoft.fooddelivery.R;

public class OrderViewHolder extends RecyclerView.ViewHolder{

    public TextView tv_order_id, tv_order_status, tv_order_phone, tv_order_address;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_order_id = itemView.findViewById(R.id.order_id);
        tv_order_status = itemView.findViewById(R.id.order_status);
        tv_order_phone = itemView.findViewById(R.id.order_phone);
        tv_order_address = itemView.findViewById(R.id.order_address);
    }
}
