package com.hackzurich.homegate.adapter;

import com.hackzurich.homegate.fragment.ImageFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    List<String> urls;

    public ScreenSlidePagerAdapter(FragmentManager fm, List<String> urls) {
        super(fm);
        this.urls = urls;
    }

    @Override
    public Fragment getItem(int position) {
        ImageFragment imageFragment = new ImageFragment();
        imageFragment.setUrl(urls.get(position));
        return imageFragment;
    }

    @Override
    public int getCount() {
        return urls.size();
    }
}