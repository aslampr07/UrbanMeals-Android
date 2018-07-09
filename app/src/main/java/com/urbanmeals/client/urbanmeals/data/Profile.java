package com.urbanmeals.client.urbanmeals.data;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Profile {
    private String name;
    private String photoCount;
    private String reviewCount;
    private ArrayList<String> images;
    private Boolean isBlogger;
    private String bio;
    private String website;
    private String dpUrl;

    public String getDpUrl() {
        return dpUrl;
    }

    public void setDpUrl(String dpUrl) {
        this.dpUrl = dpUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Boolean getBlogger() {
        return isBlogger;
    }

    public void setBlogger(Boolean blogger) {
        isBlogger = blogger;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhotoCount(String photoCount) {
        this.photoCount = photoCount;
    }

    public void setReviewCount(String reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getName() {
        return name;
    }

    public String getPhotoCount() {
        return photoCount;
    }

    public String getReviewCount() {
        return reviewCount;
    }
}
