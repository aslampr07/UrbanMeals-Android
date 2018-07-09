package com.urbanmeals.client.urbanmeals.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.urbanmeals.client.urbanmeals.fragments.HotelHeaderPictureFragment;
import com.urbanmeals.client.urbanmeals.fragments.HotelHeaderFragment;
import com.urbanmeals.client.urbanmeals.data.HotelProfile;

public class HotelHeadFragmentAdapter extends FragmentPagerAdapter {

    private HotelProfile hotelProfile;

    public HotelHeadFragmentAdapter(FragmentManager fm, HotelProfile hotelProfile) {
        super(fm);
        this.hotelProfile = hotelProfile;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            HotelHeaderFragment firstPage = new HotelHeaderFragment();
            Bundle bundle = new Bundle();
            bundle.putString("name", hotelProfile.getName());
            bundle.putString("place", hotelProfile.getPlace());
            bundle.putString("phone", hotelProfile.getPhone());
            bundle.putDouble("latitude", hotelProfile.getLatitude());
            bundle.putDouble("longitude", hotelProfile.getLongitude());
            firstPage.setArguments(bundle);
            return firstPage;
        } else if (position > 0) {
            HotelHeaderPictureFragment picture = new HotelHeaderPictureFragment();
            Bundle args = new Bundle();
            args.putString("url", hotelProfile.getImages().get(position - 1));
            picture.setArguments(args);
            return picture;
        }
        return null;
    }

    @Override
    public int getCount() {
        return hotelProfile.getImages().size() + 1;
    }
}
