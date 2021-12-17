package com.c323FinalProject.colejmetzger.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.c323FinalProject.colejmetzger.R;
import com.c323FinalProject.colejmetzger.adapters.RecentOrdersAdapter;
import com.c323FinalProject.colejmetzger.types.Order;
import com.c323FinalProject.colejmetzger.types.OrderItem;
import com.c323FinalProject.colejmetzger.utilities.DatabaseHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecentOrdersFragment#} factory method to
 * create an instance of this fragment.
 */
public class RecentOrdersFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    DatabaseHelper databaseHelper;
    List<Order> orderList;

    public RecentOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recent_orders, container, false);

        //Instantiate order list
        orderList = new ArrayList<>();

        databaseHelper = new DatabaseHelper(getContext());

        recyclerView = view.findViewById(R.id.recyclerViewRecentOrders);
        getOrderListAndSort();
        RecentOrdersAdapter recentOrdersAdapter = new RecentOrdersAdapter(getContext(), orderList);
        recyclerView.setAdapter(recentOrdersAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }


    // sort recent orders by date
    private void getOrderListAndSort() {
        List<Order> orderListTemp = new ArrayList<>();
        Order[] ordersWithoutItems = databaseHelper.getOrders();
        for (int i = 0; i < ordersWithoutItems.length; i++) {
            int orderID = ordersWithoutItems[i].getId();
            String restaurant = ordersWithoutItems[i].getRestaurant();
            String address = ordersWithoutItems[i].getAddress();
            String instructions = ordersWithoutItems[i].getInstructions();
            OrderItem[] orderItems = databaseHelper.getOrderItems(orderID);
            int total = ordersWithoutItems[i].getTotal();
            String date = ordersWithoutItems[i].getDate();
            String time = ordersWithoutItems[i].getTime();
            orderListTemp.add(new Order(orderID, restaurant, address, instructions, orderItems, total, date, time));
        }

        //Sort ordersList
        Collections.sort(orderListTemp, new Comparator<Order>() {
            @Override
            public int compare(Order order, Order t1) {
                return t1.getDateDate().compareTo(order.getDateDate());
            }
        });

        //Only add the 5 most recent orders to new list
        for (int i = 0; i < 5; i++) {
            orderList.add(orderListTemp.get(i));
        }
    }
}