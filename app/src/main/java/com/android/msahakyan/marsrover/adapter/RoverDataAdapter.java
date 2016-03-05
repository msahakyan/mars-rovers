package com.android.msahakyan.marsrover.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.msahakyan.marsrover.R;
import com.android.msahakyan.marsrover.activity.DetailPagerActivity;
import com.android.msahakyan.marsrover.application.AppController;
import com.android.msahakyan.marsrover.model.Photo;
import com.android.msahakyan.marsrover.model.RoverType;
import com.android.msahakyan.marsrover.util.BundleKey;
import com.android.msahakyan.marsrover.util.ItemClickListener;
import com.android.msahakyan.marsrover.view.FadeInNetworkImageView;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author msahakyan
 *         <p>
 *         Mars Rover Photos adapter
 */
public class RoverDataAdapter extends RecyclerView.Adapter<RoverDataAdapter.PhotosViewHolder> {

    private static final String TAG = RoverDataAdapter.class.getSimpleName();

    private List<Photo> mPhotoList;
    private Context mContext;
    private ImageLoader mImageLoader;

    public RoverDataAdapter(Context context, List<Photo> photos) {
        mPhotoList = photos;
        mContext = context;
        mImageLoader = AppController.getInstance().getImageLoader();
    }

    @Override
    public PhotosViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_photo_item, viewGroup, false);
        return new PhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotosViewHolder holder, int position) {
        final Photo photo = mPhotoList.get(position);
        if (photo != null) {
            holder.photoThumbnail.setImageUrl(photo.getImgSrc(), mImageLoader);
            holder.roverThumbnail.setImageResource(getRoverThumbId(photo.getRover().getName()));
            holder.roverName.setText(photo.getRover().getName());
            holder.sol.setText(mContext.getString(R.string.label_sol, photo.getSol()));
        }
        holder.setClickListener((view, position1) -> {
            Log.d(TAG, "Sending photo list to detail pager activity");
            ((ItemClickListener) mContext).onClick(view, position1);
        });
    }

    private int getRoverThumbId(String roverName) {
        if (RoverType.CURIOSITY.name().equalsIgnoreCase(roverName)) {
            return R.drawable.rover_curiosity_icon;
        } else if (RoverType.SPIRIT.name().equalsIgnoreCase(roverName)) {
            return R.drawable.rover_spirit_icon;
        } else if (RoverType.OPPORTUNITY.name().equalsIgnoreCase(roverName)) {
            return R.drawable.rover_opportunity_icon;
        } else {
            throw new IllegalArgumentException("Unsupported rover type");
        }
    }

    public void clearData() {
        mPhotoList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }

    static class PhotosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemClickListener clickListener;

        @Bind(R.id.photo_thumbnail)
        protected FadeInNetworkImageView photoThumbnail;

        @Bind(R.id.rover_thumbnail)
        protected CircleImageView roverThumbnail;

        @Bind(R.id.rover_name)
        protected TextView roverName;

        @Bind(R.id.sol)
        protected TextView sol;

        public PhotosViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            this.clickListener.onClick(v, getPosition());
        }
    }
}

