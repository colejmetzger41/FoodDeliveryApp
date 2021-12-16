package com.c323FinalProject.colejmetzger.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.c323FinalProject.colejmetzger.R;
import com.c323FinalProject.colejmetzger.fragments.MapsFragment;
import com.c323FinalProject.colejmetzger.types.Food;
import com.c323FinalProject.colejmetzger.types.Order;
import com.c323FinalProject.colejmetzger.types.Restaurant;
import com.c323FinalProject.colejmetzger.utilities.DatabaseHelper;

import java.util.Arrays;

public class RestaurantOrdersAdapter extends RecyclerView.Adapter<RestaurantOrdersAdapter.ViewHolder> {
    Context context;
    Food[] data;
    public int[] counts;

    public RestaurantOrdersAdapter(Context context, Food[] orders) {
        this.context = context;
        this.data = orders;
        this.counts = new int[data.length];
        Arrays.fill(counts, 0);
    }

    public RestaurantOrdersAdapter(Context context, Food[] orders, int[] counts) {
        this.context = context;
        this.data = orders;
        this.counts = counts;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, price, quantity; Button add, subtract;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.food_name_1);
            price = itemView.findViewById(R.id.food_price_1);
            quantity = itemView.findViewById(R.id.food_quantity_1);
            add = itemView.findViewById(R.id.add_button_1);
            subtract = itemView.findViewById(R.id.delete_button_1);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("asdf", "created one view holder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(data[holder.getAdapterPosition()].getName()+", ");
        holder.price.setText("$" + String.valueOf(data[holder.getAdapterPosition()].getPrice()) + ", ");
        holder.quantity.setText(String.valueOf(counts[holder.getAdapterPosition()]));
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counts[holder.getAdapterPosition()]++;
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

        holder.subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counts[holder.getAdapterPosition()]!=0) {
                    counts[holder.getAdapterPosition()]--;
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }
}
