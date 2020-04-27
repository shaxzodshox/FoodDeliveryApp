package com.shlsoft.fooddelivery.model;

public class Category {
    private String Name;
    private String Image;

    public Category() {
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setImage(String image) {
        this.Image = image;
    }

    public String getImage() {
        return Image;
    }
}
