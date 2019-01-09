package com.example.ibrahim.resfor.Restaurant;

public class menuItem  {
    private String Name,Description,price,image;

    public menuItem(){}
    public menuItem(String name, String description, String price,String image) {
        Name = name;
        Description = description;
        this.price = price;
        this.image=image;
    }

    public String getImage() {
        return image;
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
