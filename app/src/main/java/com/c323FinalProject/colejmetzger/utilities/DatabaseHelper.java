package com.c323FinalProject.colejmetzger.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.c323FinalProject.colejmetzger.R;
import com.c323FinalProject.colejmetzger.types.Order;
import com.c323FinalProject.colejmetzger.types.OrderItem;
import com.c323FinalProject.colejmetzger.types.Restaurant;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHelper {

    private static DatabaseHelper INSTANCE;
    private Context context;
    public SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        this.context = context;
        setUpDb();

    }

    private void setUpDb() {
        File dbFile = context.getDatabasePath("db.db");
        if(!dbFile.exists()) {
            try {
                db = context.openOrCreateDatabase("db", context.MODE_PRIVATE, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS Orders " +
                        "(id integer primary key AUTOINCREMENT, restaurant VARCHAR, address VARCHAR, instructions VARCHAR, total integer, date VARCHAR, time VARCHAR);");
                db.execSQL("CREATE TABLE IF NOT EXISTS OrderItems " +
                        "(id integer primary key AUTOINCREMENT, foodName VARCHAR, quantity VARCHAR, orderId INTEGER);");
                db.execSQL("CREATE TABLE IF NOT EXISTS Restaurants " +
                        "(id integer primary key AUTOINCREMENT, name VARCHAR, location VARCHAR, " +
                        "imageOne VARCHAR, imageTwo VARCHAR, imageThree VARCHAR);");
                db.execSQL("CREATE TABLE IF NOT EXISTS Foods " +
                        "(id integer primary key AUTOINCREMENT, name VARCHAR, price integer);");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void populateDB() {
        db = context.openOrCreateDatabase("db", android.content.Context.MODE_PRIVATE, null);
        String[] restaurantQueries = context.getResources().getStringArray(R.array.restaurant_populate_queries);
        String[] orderQueries = context.getResources().getStringArray(R.array.orders_populate_queries);
        String[] ordersPageQueries = context.getResources().getStringArray(R.array.orders_page_populate_queries);
        String[] foodQueries = context.getResources().getStringArray(R.array.food_populate_queries);

        for (int i = 0; i < restaurantQueries.length; i++) {
            db.execSQL(restaurantQueries[i]);
        }
        for (int i = 0; i < orderQueries.length; i++) {
            db.execSQL(orderQueries[i]);
        }
        for (int i = 0; i < ordersPageQueries.length; i++) {
            db.execSQL(ordersPageQueries[i]);
        }
        for (int i = 0; i < foodQueries.length; i++) {
            db.execSQL(foodQueries[i]);
        }
    }

    public static void createInstance(Context ct) {
        INSTANCE = new DatabaseHelper(ct);
    }

    public static DatabaseHelper getInstance() {
        if (INSTANCE == null) {
            Log.d("asdf", "instance not created yet");
            return null;
        }
        return INSTANCE;
    }

    public void insertOrder(String restaurantName, String address, String instructions, int total, String date, String time) {
        db = context.openOrCreateDatabase("db", android.content.Context.MODE_PRIVATE, null);
        String baseQuery = String.format(
                "INSERT INTO Orders (restaurant, address, instructions, total, date, time) VALUES ('%s','%s','%s', %d, '%s','%s');",
                restaurantName, address, instructions, total, date, time);
        db.execSQL(baseQuery);
        Toast.makeText(context, "Order placed", Toast.LENGTH_LONG).show();
    }

    public void insertOrderItem(int foodId, int quantity) {
        db = context.openOrCreateDatabase("db", android.content.Context.MODE_PRIVATE, null);
        String baseQuery = String.format(
                "INSERT INTO OrderItems (foodName, quantity) VALUES ('%s','%s');",
                String.valueOf(foodId), String.valueOf(quantity));
        db.execSQL(baseQuery);
    }

    public Order[] getOrders() {
        Cursor query = db.query("Orders", null, null, null, null, null, null);
        ArrayList<Order> lst = new ArrayList<Order>();

        try {
            while(query.moveToNext()) {
                @SuppressLint("Range") String id = query.getString(query.getColumnIndex("id"));
                @SuppressLint("Range") String restaurant = query.getString(query.getColumnIndex("restaurant"));
                @SuppressLint("Range") String address = query.getString(query.getColumnIndex("address"));
                @SuppressLint("Range") String instructions = query.getString(query.getColumnIndex("instructions"));
                @SuppressLint("Range") String total = query.getString(query.getColumnIndex("total"));
                @SuppressLint("Range") String date = query.getString(query.getColumnIndex("date"));
                @SuppressLint("Range") String time = query.getString(query.getColumnIndex("time"));
                lst.add(new Order(Integer.parseInt(id), restaurant, address, instructions, Integer.parseInt(total), date, time));
            }
        } finally {
            query.close();
        }

        return lst.toArray(new Order[0]);
    }
    
    public Restaurant[] getRecentRestaurants() {
        Order[] orders = getOrders();
        Order[] recentOrders = Arrays.copyOfRange(orders, orders.length - 5, orders.length);
        Restaurant[] allRestaurants = getRestaurants();
        ArrayList recentRestaurants = new ArrayList<Restaurant>();

        for (int i = 0; i < recentOrders.length; i++) {
            for (int j = 0; j < allRestaurants.length; j++) {
                if (recentOrders[i].getRestaurant().equals(allRestaurants[j].getName())) {
                    recentRestaurants.add(allRestaurants[j]);
                }
            }
        }
        return (Restaurant[]) recentRestaurants.toArray(new Restaurant[0]);
    }

    public OrderItem[] getOrderItems(int toFindOrderId) {
        Cursor query = db.query("OrderItems", null, null, null, null, null, null);
        ArrayList<OrderItem> lst = new ArrayList<OrderItem>();

        try {
            while(query.moveToNext()) {
                @SuppressLint("Range") String id = query.getString(query.getColumnIndex("id"));
                @SuppressLint("Range") String foodName = query.getString(query.getColumnIndex("foodName"));
                @SuppressLint("Range") String quantity = query.getString(query.getColumnIndex("quantity"));
                @SuppressLint("Range") String orderId = query.getString(query.getColumnIndex("quantity"));
                lst.add(new OrderItem(Integer.parseInt(id), Integer.parseInt(orderId), foodName, Integer.parseInt(quantity)));
            }
        } finally {
            query.close();
        }

        ArrayList<OrderItem> filteredList = new ArrayList<OrderItem>();
        for (int i = 0; i < lst.size(); i++) {
            if (lst.get(i).getOrderId() == toFindOrderId) {
                filteredList.add(lst.get(i));
            }
        }

        return filteredList.toArray(new OrderItem[0]);
    }

    public Restaurant[] getRestaurants() {
        Cursor query = db.query("Restaurants", null, null, null, null, null, null);
        ArrayList<Restaurant> lst = new ArrayList<Restaurant>();

        try {
            while(query.moveToNext()) {
                @SuppressLint("Range") String id = query.getString(query.getColumnIndex("id"));
                @SuppressLint("Range") String name = query.getString(query.getColumnIndex("name"));
                @SuppressLint("Range") String location = query.getString(query.getColumnIndex("location"));
                @SuppressLint("Range") String imageOne = query.getString(query.getColumnIndex("imageOne"));
                @SuppressLint("Range") String imageTwo = query.getString(query.getColumnIndex("imageTwo"));
                @SuppressLint("Range") String imageThree = query.getString(query.getColumnIndex("imageThree"));
                lst.add(new Restaurant(Integer.parseInt(id), name, location, imageOne, imageTwo, imageThree));
            }
        } finally {
            query.close();
        }

        return lst.toArray(new Restaurant[0]);
    }

    public Order getOrderById(int toFindId) {
        Cursor query = db.query("Orders", null, null, null, null, null, null);
        ArrayList<Order> lst = new ArrayList<Order>();

        try {
            while(query.moveToNext()) {
                @SuppressLint("Range") String id = query.getString(query.getColumnIndex("id"));
                @SuppressLint("Range") String restaurant = query.getString(query.getColumnIndex("restaurant"));
                @SuppressLint("Range") String address = query.getString(query.getColumnIndex("address"));
                @SuppressLint("Range") String instructions = query.getString(query.getColumnIndex("instructions"));
                @SuppressLint("Range") String total = query.getString(query.getColumnIndex("total"));
                @SuppressLint("Range") String date = query.getString(query.getColumnIndex("date"));
                @SuppressLint("Range") String time = query.getString(query.getColumnIndex("time"));
                lst.add(new Order(Integer.parseInt(id), restaurant, address, instructions, Integer.parseInt(total), date, time));
            }
        } finally {
            query.close();
        }

        for (Order order: lst) {
            if (order.getId() == toFindId) {
                return order;
            }
        }

        Toast.makeText(context, "Couldn't find order", Toast.LENGTH_LONG).show();

        return null;
    }

    public Restaurant getRestaurantByName(String toFindName) {
        Cursor query = db.query("Restaurants", null, null, null, null, null, null);
        ArrayList<Restaurant> lst = new ArrayList<Restaurant>();

        try {
            while(query.moveToNext()) {
                @SuppressLint("Range") String id = query.getString(query.getColumnIndex("id"));
                @SuppressLint("Range") String name = query.getString(query.getColumnIndex("name"));
                @SuppressLint("Range") String location = query.getString(query.getColumnIndex("location"));
                @SuppressLint("Range") String imageOne = query.getString(query.getColumnIndex("imageOne"));
                @SuppressLint("Range") String imageTwo = query.getString(query.getColumnIndex("imageTwo"));
                @SuppressLint("Range") String imageThree = query.getString(query.getColumnIndex("imageThree"));
                lst.add(new Restaurant(Integer.parseInt(id), name, location, imageOne, imageTwo, imageThree));
            }
        } finally {
            query.close();
        }

        for (Restaurant restaurant: lst) {
            if (restaurant.getName().equals(toFindName)) {
                return restaurant;
            }
        }

        Toast.makeText(context, "Couldn't restaurant by name", Toast.LENGTH_LONG).show();

        return null;
    }
}
