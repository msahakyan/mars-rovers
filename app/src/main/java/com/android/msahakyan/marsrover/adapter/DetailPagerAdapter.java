package com.android.msahakyan.marsrover.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.msahakyan.marsrover.fragment.DetailFragment;
import com.android.msahakyan.marsrover.model.Photo;
import com.android.msahakyan.marsrover.util.BundleKey;

import java.util.List;

/**
 * @author msahakyan
 */
public class DetailPagerAdapter extends FragmentStatePagerAdapter {

    private List<Photo> mPhotos;

    public DetailPagerAdapter(FragmentManager fragmentManager, List<Photo> photos) {
        super(fragmentManager);
        mPhotos = photos;
    }

    // Returns a total number of pages
    @Override
    public int getCount() {
        return mPhotos.size();
    }

    // Returns a fragment to display
    @Override
    public Fragment getItem(int position) {
        DetailFragment fragment = DetailFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKey.EXTRA_PHOTO, mPhotos.get(position));
        fragment.setArguments(bundle);
        return fragment;
    }

    // Returns a page title
    @Override
    public CharSequence getPageTitle(int position) {
        return mPhotos.get(position).getCamera().getName();
    }

    // Returns whether athe current photo has next element or not
    private boolean hasNext(int position) {
        return (mPhotos.size() > 1) && (position < mPhotos.size() - 1);
    }
}
