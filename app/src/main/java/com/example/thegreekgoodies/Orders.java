package com.example.thegreekgoodies;

public class Orders {
    private String orderId;
    private String userId;
    private String productname;
    private int quantity;
    private double totalprice;
    private String status;
    private String rider;
    private int set;

    public Orders(String orderId, String userId, String productname, int quantity, double totalprice, String status, String rider, int set) {
        this.orderId = orderId;
        this.userId = userId;
        this.productname = productname;
        this.quantity = quantity;
        this.totalprice = totalprice;
        this.status = status;
        this.rider = rider;
        this.set = set;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(double totalprice) {
        this.totalprice = totalprice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }
}
