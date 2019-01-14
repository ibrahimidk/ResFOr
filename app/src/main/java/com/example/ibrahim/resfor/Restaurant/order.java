package com.example.ibrahim.resfor.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class order {
    String location,name,number;
    List<menuItem> orderList;

    public order(){

    }

    public order(String location, String name, String number, List<menuItem> theOrder) {
        this.location = location;
        this.name = name;
        this.number = number;
        this.orderList = new ArrayList<>();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<menuItem> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<menuItem> orderList) {
        this.orderList = orderList;
    }
}
