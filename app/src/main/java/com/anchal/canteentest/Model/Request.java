package com.anchal.canteentest.Model;

import java.util.List;

// When we will order food, then it will be stored in 'request' file in EatItDB database.

public class Request {
    private String phone;
    private String name;
    private String address;
    private String total;
    private List<Order> foods;
    private String status;
    private String distance;


    public Request() {
    }

    public Request(String phone, String name, String address, String total, List<Order> foods, String distance) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.foods = foods;
        this.status = "0";
        this.distance = distance;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }
}
