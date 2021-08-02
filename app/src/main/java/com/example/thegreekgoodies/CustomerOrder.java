package com.example.thegreekgoodies;

import java.io.Serializable;

public class CustomerOrder implements Serializable {
    private int _id;
    String itemName;
    String customerName;
    int quantity;
    double itemPrice;

    public CustomerOrder(int _id, String itemName, String customerName, int quantity, double itemPrice) {
        this._id = _id;
        this.itemName = itemName;
        this.customerName = customerName;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }

    public int get_id() {
        return _id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getItemPrice() {
        return itemPrice;
    }
}
