package com.urbanmeals.client.urbanmeals.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.urbanmeals.client.urbanmeals.PromotionBannerFragment;
import com.urbanmeals.client.urbanmeals.data.PromotionBanner;

import java.util.ArrayList;


public class PromotionBannerFragmentAdapter extends FragmentStatePagerAdapter {

    private ArrayList<PromotionBanner> list;

    public PromotionBannerFragmentAdapter(FragmentManager fm, ArrayList<PromotionBanner> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("bannerURL", list.get(position).getUrl());
        bundle.putString("hotelCode", list.get(position).getHotelCode());

        PromotionBannerFragment fragment = new PromotionBannerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}