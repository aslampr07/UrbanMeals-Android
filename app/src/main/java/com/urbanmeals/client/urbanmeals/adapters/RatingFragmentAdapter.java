package com.urbanmeals.client.urbanmeals.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.urbanmeals.client.urbanmeals.fragments.OverallRatingFragment;
import com.urbanmeals.client.urbanmeals.fragments.StatisticsFragment;

/**
 * Created by aslampr07 on 24/3/18.
 */

public class RatingFragmentAdapter extends FragmentPagerAdapter {

    private CharSequence[] titles = {"Overall Rating", "Statistics"};
    private String hotelCode;

    public RatingFragmentAdapter(FragmentManager fm, String hotelCode) {
        super(fm);
        this.hotelCode = hotelCode;
    }

    @Override
    public Fragment getItem(int position) {
        OverallRatingFragment overallRatingFragment = new OverallRatingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("hotelCode", hotelCode);
        overallRatingFragment.setArguments(bundle);
        switch (position) {
            case 0:
                return overallRatingFragment;
            case 1:
                return new StatisticsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
