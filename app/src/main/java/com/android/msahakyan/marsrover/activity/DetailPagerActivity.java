package com.android.msahakyan.marsrover.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.msahakyan.marsrover.R;
import com.android.msahakyan.marsrover.adapter.DetailPagerAdapter;
import com.android.msahakyan.marsrover.model.Photo;
import com.android.msahakyan.marsrover.util.BundleKey;
import com.android.msahakyan.marsrover.util.ZoomOutPageTransformer;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailPagerActivity extends AppCompatActivity {

    private static final String TAG = DetailPagerActivity.class.getSimpleName();

    @Bind(R.id.vpPager)
    protected ViewPager mViewPager;

    private int mStartPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pager);

        // Initialization ButterKnife
        ButterKnife.bind(this);

        Log.d(TAG, "Receiving list of photos from intent");
        List<Photo> photoList = getIntent().getParcelableArrayListExtra(BundleKey.EXTRA_PHOTOS_TO_BE_SEND);
        ;

        mStartPosition = getIntent().getIntExtra(BundleKey.EXTRA_START_POSITION, 0);

        DetailPagerAdapter mAdapterVp = new DetailPagerAdapter(getSupportFragmentManager(), photoList);
        mViewPager.setAdapter(mAdapterVp);

        // Attach the page change listener inside the activity
        mViewPager.addOnPageChangeListener(mPageChangeListener);

        // Attach page transformer
        mViewPager.setPageTransformer(false, new ZoomOutPageTransformer());
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.SimpleOnPageChangeListener();

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        int absolutePosition = mStartPosition + mViewPager.getCurrentItem();
        returnIntent.putExtra(BundleKey.EXTRA_POSITION, absolutePosition);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
