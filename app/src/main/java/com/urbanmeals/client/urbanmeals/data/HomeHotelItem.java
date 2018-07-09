package com.urbanmeals.client.urbanmeals.data;

/**
 * Created by aslampr07 on 5/3/18.
 */

public class HomeHotelItem {
    private String name;
    private boolean isOpened;
    private Double rating;
    private float distance;
    private String isVeg;
    private String hotelCode;

    public HomeHotelItem(String name, boolean isOpened, Double rating, float distance, String isVeg, String HotelCode) {
        this.name = name;
        this.isOpened = isOpened;
        this.rating = rating;
        this.distance = distance;
        this.isVeg = isVeg;
        this.hotelCode = HotelCode;
        this.rating = rating;
    }

    public String getHotelCode() {
        return hotelCode;
    }

    public String getHotelName() {
        return this.name;
    }

    public boolean getOpened() {
        return this.isOpened;
    }

    public Double getRating() {
        return this.rating;
    }

    public float getDistance() {
        return this.distance;
    }

    public String getIsVeg() {
        return this.isVeg;
    }
}
