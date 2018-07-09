package com.urbanmeals.client.urbanmeals;

import android.content.SearchRecentSuggestionsProvider;

public class HotelSearchSuggestionProvider extends SearchRecentSuggestionsProvider {
    public static String AUTHORITY = "com.urbanmeals.client.urbanmeals.HotelSearchSuggestionProvider";
    public static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;

    public HotelSearchSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
