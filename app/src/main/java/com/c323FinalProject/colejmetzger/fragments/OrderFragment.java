package com.c323FinalProject.colejmetzger.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

    int orderId;
    Order order;
    OrderItem[] orderItems;

    public OrderFragment(int orderId) {
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

        TextView address, time, total; Button trackOrder;
        address = v.findViewById(R.id.address_order);
        time = v.findViewById(R.id.date_time);
        total = v.findViewById(R.id.total_order);
        trackOrder = v.findViewById(R.id.track_order_button);

        address.setText(order.getAddress());
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        time.setText(dateFormat.format(date));
        total.setText("Total: $" + String.valueOf(order.getTotal()));


        // add foods to display
        LinearLayout group = v.findViewById(R.id.insert_point);
        for( int i = 0; i < orderItems.length; i++ )
        {
            TextView textView = new TextView(getActivity());
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(p);
            textView.setText(orderItems[i].getFoodName() + ", x"+ String.valueOf(orderItems[i].getQuantity()));
            textView.setId(10+i);
            group.addView(textView);
        }

        // go to maps and send to and from info
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

        return v;
    }
}