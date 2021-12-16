package com.c323FinalProject.colejmetzger.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.c323FinalProject.colejmetzger.R;
import com.c323FinalProject.colejmetzger.types.Order;
import com.c323FinalProject.colejmetzger.types.OrderItem;
import com.c323FinalProject.colejmetzger.types.Restaurant;
import com.c323FinalProject.colejmetzger.utilities.DatabaseHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class OrderFragment extends Fragment {

    int orderId; // = 6; temp hardcoded, just getting it running so you can do maps
    Order order;
    OrderItem[] orderItems;

    public OrderFragment(int orderId) {
        // Required empty public constructor
        this.orderId = orderId;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: get order id from fragment transition
        order = DatabaseHelper.getInstance().getOrderById(orderId);
        orderItems = DatabaseHelper.getInstance().getOrderItems(orderId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_order, container, false);

        TextView address, time, total; ViewGroup orderItems; Button trackOrder;
        address = v.findViewById(R.id.address_order);
        time = v.findViewById(R.id.date_time);
        total = v.findViewById(R.id.total_order);
        trackOrder = v.findViewById(R.id.track_order_button);

        address.setText(order.getAddress());
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        time.setText(dateFormat.format(date));
        total.setText(String.valueOf(order.getTotal()));

        trackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("userAddress", order.getAddress());
                Restaurant res = DatabaseHelper.getInstance().getRestaurantByName(order.getRestaurant());
                bundle.putString("restaurantAddress", res.getLocation() );
                MapsFragment mapsFrag = new MapsFragment();
                mapsFrag.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, mapsFrag, "maps")
                        .commit();
            }
        });


        // TODO: loop through items and do the following two lines to add food items to order display
//        ViewGroup main = (ViewGroup) findViewById(R.id.insert_point);
//        main.addView(view, 0);

        return v;
    }
}