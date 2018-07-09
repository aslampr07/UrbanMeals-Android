package com.urbanmeals.client.urbanmeals.interfaces;

import com.android.volley.VolleyError;
import com.urbanmeals.client.urbanmeals.adapters.HomeHotelListAdapter;

public interface CardListReadyListener {
    void onCompleted(HomeHotelListAdapter adapter);
    void onError(VolleyError error);
}
