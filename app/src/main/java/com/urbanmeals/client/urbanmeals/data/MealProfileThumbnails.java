package com.urbanmeals.client.urbanmeals.data;

public class MealProfileThumbnails {
    private String thumbnailURL;
    private String imageCode;

    public MealProfileThumbnails(String thumbnailURL, String imageCode) {
        this.thumbnailURL = thumbnailURL;
        this.imageCode = imageCode;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public String getImageCode() {
        return imageCode;
    }
}
