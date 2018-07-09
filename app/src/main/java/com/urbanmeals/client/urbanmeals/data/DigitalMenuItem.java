package com.urbanmeals.client.urbanmeals.data;

/**
 * Created by aslampr07 on 18/3/18.
 */

public class DigitalMenuItem {
    private String name;
    private Float price;
    private double rating;

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    private String code;

    public DigitalMenuItem(String name, Float price, String code) {
        this.name = name;
        this.price = price;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public Float getPrice() {
        return price;
    }

    public String getCode() { return code; }

}
