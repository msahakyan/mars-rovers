package com.android.msahakyan.marsrover.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.msahakyan.marsrover.R;
import com.android.msahakyan.marsrover.application.AppController;
import com.android.msahakyan.marsrover.model.Photo;
import com.android.msahakyan.marsrover.util.BundleKey;
import com.android.msahakyan.marsrover.view.FadeInNetworkImageView;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author msahakyan
 */
public class DetailFragment extends Fragment {

    private static final String TAG = DetailFragment.class.getSimpleName();

    @Bind(R.id.detail_photo_thumbnail)
    protected FadeInNetworkImageView mDetailImage;

    public DetailFragment() {
    }

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo_detail, container, false);
        ButterKnife.bind(this, view);
        setUpPhotoDetails();

        return view;
    }

    private void setUpPhotoDetails() {
        if (getArguments().containsKey(BundleKey.EXTRA_PHOTO)) {
            Photo photo = getArguments().getParcelable(BundleKey.EXTRA_PHOTO);
            if (photo != null) {
                mDetailImage.setImageUrl(photo.getImgSrc(), AppController.getInstance().getImageLoader());
            }
        }
    }
}
