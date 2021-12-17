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
    int [] counts;


    public RestaurantFragment(String name) { ;
        this.restaurant = getRestaurant(name);
        getFood();
    }

    public RestaurantFragment(String name, int[] counts, Food[] foods) { ;
        this.restaurant = getRestaurant(name);
        this.counts = counts;
        this.foods = foods;
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
        //
        View v = inflater.inflate(R.layout.fragment_restaurant, container, false);
        Button checkout = v.findViewById(R.id.checkout_button);

        // on checkout send data to checkout fragment
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get foods that arent empty
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


                // go to checkout fragment
                CheckoutFragment checkoutFragment = new CheckoutFragment(
                        (Food[]) addedFoods.toArray(new Food[0]),
                        arr,
                        restaurant
                );
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, checkoutFragment, "checkout")
                        .commit();
            }
        });


        // depending on wether or not the user returning from checkout or is starting here, use different adapter constructor
        recyclerView = v.findViewById(R.id.restaurant_foods_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (this.counts == null) {
            adapter = new RestaurantOrdersAdapter(getContext(), foods);
        } else {
            adapter = new RestaurantOrdersAdapter(getContext(), foods, counts);
        }
        recyclerView.setAdapter(adapter);
        return v;
    }
}