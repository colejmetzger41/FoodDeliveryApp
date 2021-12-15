package com.c323FinalProject.colejmetzger.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.c323FinalProject.colejmetzger.MainActivity;
import com.c323FinalProject.colejmetzger.R;
import com.c323FinalProject.colejmetzger.fragments.RestaurantFragment;
import com.c323FinalProject.colejmetzger.fragments.RestaurantImages;
import com.c323FinalProject.colejmetzger.types.Restaurant;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ViewHolder> {
    Context context;
    Restaurant[] data;

    public RecentAdapter(Context context, Restaurant[] data) {
        this.context = context;
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView restaurantName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantName = itemView.findViewById(R.id.home_all_text);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("asdf", "created one recent holder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_all, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.restaurantName.setText(data[holder.getAdapterPosition()].getName());


        // on click transition to restaurant frag
        holder.restaurantName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activityClass = (MainActivity) context;

//                Bundle bundle = new Bundle();
//                bundle.putString("restaurantId", data[holder.getAdapterPosition()].getName());

                RestaurantImages imagesFrag = new RestaurantImages(data[holder.getAdapterPosition()].getName());
                RestaurantFragment restaurantFragment = new RestaurantFragment(data[holder.getAdapterPosition()].getName());
//                imagesFrag.setArguments(bundle);
//                restaurantFragment.setArguments(bundle);

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
