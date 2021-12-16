package com.c323FinalProject.colejmetzger.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.c323FinalProject.colejmetzger.MainActivity;
import com.c323FinalProject.colejmetzger.R;
import com.c323FinalProject.colejmetzger.adapters.RestaurantImagesAdapter;
import com.c323FinalProject.colejmetzger.adapters.RestaurantOrdersAdapter;
import com.c323FinalProject.colejmetzger.types.Food;
import com.c323FinalProject.colejmetzger.types.Restaurant;
import com.c323FinalProject.colejmetzger.utilities.DatabaseHelper;

import java.util.ArrayList;


public class RestaurantFragment extends Fragment {
    Restaurant restaurant;
    Food[] foods;
    RecyclerView recyclerView;
    RestaurantOrdersAdapter adapter;


    public RestaurantFragment(String name) { ;
        this.restaurant = getRestaurant(name);
        getFood();
    }

    private Restaurant getRestaurant(String name) {
        return DatabaseHelper.getInstance().getRestaurantByName(name);
    }

    private void getFood() {
        foods = DatabaseHelper.getInstance().getFood();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_restaurant, container, false);
        Button checkout = v.findViewById(R.id.checkout_button);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] oldCounts = adapter.counts;
                ArrayList addedFoods = new ArrayList<Food>();
                ArrayList counts = new ArrayList<Integer>();

                for (int i = 0; i < oldCounts.length; i++) {
                    if (oldCounts[i]!=0) {
                        addedFoods.add(foods[i]);
                        counts.add(oldCounts[i]);
                    }
                }

                int[] arr = counts.stream().mapToInt(i -> (int) i).toArray();


                CheckoutFragment checkoutFragment = new CheckoutFragment((Food[]) addedFoods.toArray(new Food[0]), arr);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, checkoutFragment, "checkout")
                        .commit();
            }
        });


        recyclerView = v.findViewById(R.id.restaurant_foods_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RestaurantOrdersAdapter(getContext(), foods);
        recyclerView.setAdapter(adapter);

        return v;
    }
}