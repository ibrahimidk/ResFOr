package com.example.ibrahim.resfor.Restaurant;

public class menuItem  {
    private String Name,Description,price;

    public menuItem(String name, String description, String price) {
        Name = name;
        Description = description;
        this.price = price;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
