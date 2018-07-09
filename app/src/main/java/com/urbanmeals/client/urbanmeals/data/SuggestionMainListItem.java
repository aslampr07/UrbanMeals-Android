package com.urbanmeals.client.urbanmeals.data;

import java.util.ArrayList;

public class SuggestionMainListItem {
    private String place;
    private ArrayList<SuggestionSubListItem> suggestionSubList;

    public ArrayList<SuggestionSubListItem> getSuggestionSubList() {
        return suggestionSubList;
    }

    public void setSuggestionSubList(ArrayList<SuggestionSubListItem> suggestionSubList) {
        this.suggestionSubList = suggestionSubList;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
