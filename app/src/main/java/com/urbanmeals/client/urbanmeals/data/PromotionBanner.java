package com.urbanmeals.client.urbanmeals.data;

public class PromotionBanner {
    private String url;
    private String hotelCode;
    private String name;


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getHotelCode() {
        return hotelCode;
    }
    public void setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
    }
}
