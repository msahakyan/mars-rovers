package com.android.msahakyan.marsrover.fragment;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.msahakyan.marsrover.R;
import com.android.msahakyan.marsrover.adapter.RoverAdapter;
import com.android.msahakyan.marsrover.model.Rover;
import com.android.msahakyan.marsrover.service.LoaderDataSource;
import com.android.msahakyan.marsrover.util.OnRoverSelectedListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectRoverFragment extends DialogFragment {

    private static final String TAG = SelectRoverFragment.class.getSimpleName();
    private static final boolean DEBUG = true;

    private OnRoverSelectedListener mListener;
    private List<Rover> mRovers;
    private RoverAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Bind(R.id.rover_list)
    protected RecyclerView mRecyclerView;

    public SelectRoverFragment() {
        // Required empty public constructor
    }

    public static SelectRoverFragment newInstance() {
        return new SelectRoverFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRovers = LoaderDataSource.getInstance().getRoverList();
        mLayoutManager = new LinearLayoutManager(getActivity());
        setCancelable(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_select_rover, container, false);
        ButterKnife.bind(this, root);

        // Set background semi transparent
        final Drawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.speed_dial_background));
        getDialog().getWindow().setBackgroundDrawable(colorDrawable);

        // Set fragment title
        getDialog().setTitle(R.string.select_rover_header);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RoverAdapter(getActivity(), mRovers, mListener, this);
        mRecyclerView.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnRoverSelectedListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        ButterKnife.unbind(this);
    }
}
