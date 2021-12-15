package com.c323FinalProject.colejmetzger.types;

public class Order {
    int id; String restaurant, instructions, address; OrderItem[] items; int total;
    String date, time;

    // when initializing with food items
    public Order(int id, String restaurant, String address, String instructions, OrderItem[] items, int total, String date, String time) {
        this.id = id;
        this.restaurant = restaurant;
        this.instructions = instructions;
        this.items = items;
        this.total = total;
        this.address = address;
        this.date = date;
        this.time = time;
    }

    // initializing without food items
    public Order(int id, String restaurant, String address, String instructions, int total, String date, String time) {
        this.id = id;
        this.restaurant = restaurant;
        this.instructions = instructions;
        this.total = total;
        this.address = address;
        this.date = date;
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public OrderItem[] getItems() {
        return items;
    }

    public void setItems(OrderItem[] items) {
        this.items = items;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
