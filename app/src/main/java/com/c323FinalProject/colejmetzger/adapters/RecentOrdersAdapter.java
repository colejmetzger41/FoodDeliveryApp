package com.c323FinalProject.colejmetzger.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.c323FinalProject.colejmetzger.types.Order;
import com.c323FinalProject.colejmetzger.R;

import java.util.List;

public class RecentOrdersAdapter extends RecyclerView.Adapter<RecentOrdersAdapter.ViewHolder> {

    private Context context;

    public RecentOrdersAdapter(Context context, List<Order> orderList) {

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


        //Set order again button listener
        holder.button_orderAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
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
