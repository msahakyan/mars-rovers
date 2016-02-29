package com.android.msahakyan.marsrover.adapter;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.msahakyan.marsrover.R;
import com.android.msahakyan.marsrover.model.Rover;
import com.android.msahakyan.marsrover.util.ItemClickListener;
import com.android.msahakyan.marsrover.util.OnRoverSelectedListener;
import com.android.msahakyan.marsrover.util.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author msahakyan
 *         <p/>
 *         Mars Rovers adapter
 */
public class RoverAdapter extends RecyclerView.Adapter<RoverAdapter.RoverHolder> {

    private static final String TAG = RoverAdapter.class.getSimpleName();
    private static final boolean DEBUG = true;

    private List<Rover> mRoverList;
    private OnRoverSelectedListener mListener;
    private Context mContext;
    private DialogFragment mCallerFragment;

    public RoverAdapter(Context context, List<Rover> rovers, OnRoverSelectedListener listener, DialogFragment callerFragment) {
        mRoverList = rovers;
        mContext = context;
        mListener = listener;
        mCallerFragment = callerFragment;
    }

    @Override
    public RoverHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_rover_item, viewGroup, false);
        return new RoverHolder(view);
    }

    @Override
    public void onBindViewHolder(RoverHolder holder, int position) {
        final Rover rover = mRoverList.get(position);
        if (rover != null) {
            holder.thumbnail.setImageResource(Utils.getRoverThumbId(rover.getName()));
            holder.name.setText(rover.getName());
            holder.landingDate.setText(mContext.getString(R.string.landing_date, rover.getLandingDate()));
            holder.lastDate.setText(mContext.getString(R.string.last_date, rover.getMaxDate()));
        }
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (DEBUG) Log.i(TAG, "Selected rover is " + rover.getName());
                if (mListener != null) {
                    mCallerFragment.dismiss();
                    mListener.onRoverSelected(rover);
                }
            }
        });
    }

    public void clearData() {
        mRoverList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mRoverList.size();
    }

    static class RoverHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemClickListener clickListener;

        @Bind(R.id.rover_thumbnail)
        protected CircleImageView thumbnail;

        @Bind(R.id.rover_name)
        protected TextView name;

        @Bind(R.id.rover_landing_date)
        protected TextView landingDate;

        @Bind(R.id.rover_last_date)
        protected TextView lastDate;

        public RoverHolder(View view) {
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

