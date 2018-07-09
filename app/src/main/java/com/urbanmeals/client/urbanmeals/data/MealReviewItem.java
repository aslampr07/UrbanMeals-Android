package com.urbanmeals.client.urbanmeals.data;

public class MealReviewItem {
    private String userName;
    private String reviewBody;
    private Double taste;
    private Double presentation;
    private Double quantitiy;

    public MealReviewItem() {
    }

    public void setUserName(String firstname, String lastname) {
        this.userName = firstname + " " + lastname;
    }

    public String getUserName() {
        return userName;
    }

    public String getReviewBody() {
        return reviewBody;
    }

    public void setReviewBody(String reviewBody) {
        this.reviewBody = reviewBody;
    }

    public Double getTaste() {
        return taste;
    }

    public void setTaste(Double taste) {
        this.taste = taste;
    }

    public Double getPresentation() {
        return presentation;
    }

    public void setPresentation(Double presentation) {
        this.presentation = presentation;
    }

    public Double getQuantitiy() {
        return quantitiy;
    }

    public void setQuantitiy(Double quantitiy) {
        this.quantitiy = quantitiy;
    }

}
