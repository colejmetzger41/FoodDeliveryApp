package com.c323FinalProject.colejmetzger.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.c323FinalProject.colejmetzger.R;
import com.c323FinalProject.colejmetzger.adapters.HomeAdapter;
import com.c323FinalProject.colejmetzger.adapters.RestaurantImagesAdapter;
import com.c323FinalProject.colejmetzger.types.Restaurant;
import com.c323FinalProject.colejmetzger.utilities.DatabaseHelper;


public class RestaurantImages extends Fragment {

    Restaurant restaurant;
    RecyclerView recyclerView;

    public RestaurantImages(String name) {
        this.restaurant = getRestaurant(name);

    }

    private Restaurant getRestaurant(String name) {
        return DatabaseHelper.getInstance().getRestaurantByName(name);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_restaurant_images, container, false);

        TextView tv = v.findViewById(R.id.restaurant_name_text);
        tv.setText(restaurant.getName());

        recyclerView = v.findViewById(R.id.restaurant_images_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        recyclerView.setAdapter( new RestaurantImagesAdapter(getContext(), restaurant));
        return v;
    }
}