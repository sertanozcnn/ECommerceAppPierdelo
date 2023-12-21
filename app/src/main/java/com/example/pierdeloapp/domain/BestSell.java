package com.example.pierdeloapp.domain;

import java.io.Serializable;

public class BestSell implements Serializable {

    String description;
    String img_url;
    String name;
    String type_person;
    double price;
    int rating;
    double shopping_views;
    double see_views;


    public BestSell(){

    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg_url() {
        return img_url;
    }
    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getType_person() {
        return type_person;
    }
    public void setType_person(String type_person) {
        this.type_person = type_person;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public double getShopping_views() {
        return shopping_views;
    }
    public void setShopping_views(double shopping_views) {
        this.shopping_views = shopping_views;
    }
    public double getSee_views() {
        return see_views;
    }
    public void setSee_views(double see_views) {
        this.see_views = see_views;
    }
}
