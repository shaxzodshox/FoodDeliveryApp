package com.shlsoft.fooddelivery.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.shlsoft.fooddelivery.R;
import com.shlsoft.fooddelivery.interfaces.ItemClickListener;
import com.shlsoft.fooddelivery.model.Order;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tv_cart_name, tv_price;
    public ImageView img_cart_count;

    private ItemClickListener itemClickListener;

    public void setTv_cart_name(TextView tv_cart_name) {
        this.tv_cart_name = tv_cart_name;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_cart_name = itemView.findViewById(R.id.cart_item_name);
        tv_price = itemView.findViewById(R.id.cart_item_price);
        img_cart_count = itemView.findViewById(R.id.cart_item_count);
    }

    @Override
    public void onClick(View v) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> orderList = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout, parent, false);

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Order order = orderList.get(position);

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(order.getQuantity(), Color.RED);

        holder.img_cart_count.setImageDrawable(drawable);

        long price = (Long.parseLong((order.getPrice()))) * (Long.parseLong(order.getQuantity())); //e.g: 2 items 1000 = 2  * 1000 = 2000 sum
        holder.tv_price.setText(order.getQuantity()  + " x " + order.getPrice() + " = " + price + " sum");
        holder.tv_cart_name.setText(order.getProductName());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}

