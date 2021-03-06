package com.c323FinalProject.colejmetzger.types;

public class OrderItem {
    int id, orderId; String foodName; int quantity;

    public OrderItem(int id, int orderId, String foodName, int quantity) {
        this.id = id;
        this.foodName = foodName;
        this.quantity = quantity;
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
