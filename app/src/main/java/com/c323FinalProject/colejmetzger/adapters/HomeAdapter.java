package com.c323FinalProject.colejmetzger.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.c323FinalProject.colejmetzger.MainActivity;
import com.c323FinalProject.colejmetzger.R;
import com.c323FinalProject.colejmetzger.fragments.RestaurantFragment;
import com.c323FinalProject.colejmetzger.fragments.RestaurantImages;
import com.c323FinalProject.colejmetzger.types.Restaurant;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    Context context;
    Restaurant[] data;
    String type;

    public HomeAdapter(Context context, Restaurant[] data) {
        this.context = context;
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView restaurantIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantIcon = itemView.findViewById(R.id.home_restaurant_icon);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("asdf", "created one view holder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestQueue queue = Volley.newRequestQueue(context);

        //pull row image
        ImageRequest imageRequest = new ImageRequest (data[holder.getAdapterPosition()].getImageOne(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.restaurantIcon.setImageBitmap(response);
            }
        },300,400, ImageView.ScaleType.CENTER_CROP,null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Error pulling from source.",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        queue.add(imageRequest);

        // on click transition to restaurant frag
        holder.restaurantIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activityClass = (MainActivity) context;
                RestaurantImages imagesFrag = new RestaurantImages(data[holder.getAdapterPosition()].getName());
                RestaurantFragment restaurantFragment = new RestaurantFragment(data[holder.getAdapterPosition()].getName());
                activityClass.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.topView, imagesFrag, "restaurant_picture")
                        .commit();

                activityClass.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.bottomView, restaurantFragment, "restaurant")
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.length;
    }
}
