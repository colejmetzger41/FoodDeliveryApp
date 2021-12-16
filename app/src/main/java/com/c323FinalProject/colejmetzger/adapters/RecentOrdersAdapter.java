package com.c323FinalProject.colejmetzger.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.c323FinalProject.colejmetzger.MainActivity;
import com.c323FinalProject.colejmetzger.fragments.OrderFragment;
import com.c323FinalProject.colejmetzger.types.Order;
import com.c323FinalProject.colejmetzger.R;
import com.c323FinalProject.colejmetzger.types.OrderItem;
import com.c323FinalProject.colejmetzger.utilities.DatabaseHelper;

import java.util.List;

public class RecentOrdersAdapter extends RecyclerView.Adapter<RecentOrdersAdapter.ViewHolder> {

    private Context context;
    private List<Order> orderList;
    private DatabaseHelper databaseHelper;

    public RecentOrdersAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_recent_orders, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Get current order
        Order currentOrder = orderList.get(position);

        //Get all order items and format it into a string
        OrderItem[] orderItems = currentOrder.getItems();
        String orderItemsString = "";
        for (int i = 0; i < orderItems.length; i++) {
            orderItemsString += orderItems[i].getFoodName() + "               Quantity: " + orderItems[i].getQuantity();
            orderItemsString += "\n";
        }

        //Set up all the data into the text views
        holder.tv_foodItems.setText(orderItemsString);
        holder.tv_orderedFrom.setText(holder.tv_orderedFrom.getText() + " " + currentOrder.getRestaurant());
        holder.tv_price.setText(holder.tv_price.getText() + " " + currentOrder.getTotal());
        holder.tv_date.setText(holder.tv_date.getText() + " " + currentOrder.getDate());
        holder.tv_time.setText(holder.tv_time.getText() + " " + currentOrder.getTime());
        holder.tv_address.setText(holder.tv_address.getText() + " " + currentOrder.getAddress());

        //Set order again button listener
        holder.button_orderAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.insertOrder(currentOrder.getRestaurant(), currentOrder.getAddress(), currentOrder.getInstructions(), currentOrder.getTotal(), currentOrder.getDate(), currentOrder.getTime());
                MainActivity activityClass = (MainActivity) context;
                OrderFragment orderFragment = new OrderFragment(currentOrder.getId());

                activityClass.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, orderFragment, "order")
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_foodItems;
        TextView tv_orderedFrom;
        TextView tv_price;
        TextView tv_date;
        TextView tv_time;
        TextView tv_address;
        Button button_orderAgain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_foodItems = itemView.findViewById(R.id.textViewFoodItemsRecentOrders);
            tv_orderedFrom = itemView.findViewById(R.id.textViewOrderedFromRecentOrders);
            tv_price = itemView.findViewById(R.id.textViewPriceRecentOrders);
            tv_date = itemView.findViewById(R.id.textViewDateRecentOrders);
            tv_time = itemView.findViewById(R.id.textViewTimeRecentOrders);
            tv_address = itemView.findViewById(R.id.textViewAddressRecentOrders);
            button_orderAgain = itemView.findViewById(R.id.buttonOrderAgainRecentOrders);
        }
    }
}
