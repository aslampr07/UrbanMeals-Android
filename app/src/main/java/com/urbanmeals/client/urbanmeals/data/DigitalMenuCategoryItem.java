package com.urbanmeals.client.urbanmeals.data;

/**
 * Created by aslampr07 on 15/3/18.
 */

public class DigitalMenuCategoryItem {


    private String categoryName;
    private String imageUrl;
    private String categoryCode;

    public DigitalMenuCategoryItem(String categoryName, String imageUrl) {
        this.categoryName = categoryName;
        this.imageUrl = imageUrl;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryImage() {
        return imageUrl;
    }


}

