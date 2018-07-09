package com.urbanmeals.client.urbanmeals.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.urbanmeals.client.urbanmeals.fragments.MealReviewFragment;

public class MealReviewFragmentAdapter extends FragmentPagerAdapter {

    private CharSequence[] titles =  {"Critics Reviews", "User Reviews"};
    private String itemCode;

    public MealReviewFragmentAdapter(FragmentManager fm, String itemcode) {
        super(fm);
        this.itemCode = itemcode;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:{
                MealReviewFragment criticReviews = new MealReviewFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isCritic", true);
                bundle.putString("itemcode", itemCode);
                criticReviews.setArguments(bundle);
                return criticReviews;
            }
            case 1:{
                MealReviewFragment userReviews = new MealReviewFragment();
                Bundle bundle = new Bundle();
                bundle.putString("itemcode", itemCode);
                bundle.putBoolean("isCritic", false);
                userReviews.setArguments(bundle);
                return userReviews;
            }
            default: return null;
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
