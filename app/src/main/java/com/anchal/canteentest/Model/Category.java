package com.anchal.canteentest.Model;

// This class will hold the menu category.
// i.e. The category of the menu will be shown on the homeactivity page.

public class Category {
    private String Name;
    private String Image;

    public Category() {

    }

    public Category(String name, String image) {
        Name = name;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
