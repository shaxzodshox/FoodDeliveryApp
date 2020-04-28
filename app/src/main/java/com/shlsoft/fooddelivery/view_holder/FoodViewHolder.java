package com.shlsoft.fooddelivery.view_holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shlsoft.fooddelivery.R;
import com.shlsoft.fooddelivery.interfaces.ItemClickListener;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtFoodName;
    public ImageView imgFood;

    private ItemClickListener listener;

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);

        txtFoodName = itemView.findViewById(R.id.tv_food);
        imgFood = itemView.findViewById(R.id.food_image);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.listener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
