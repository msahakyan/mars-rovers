package com.android.msahakyan.marsrover.speedDial;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.drawable.RotateDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.msahakyan.marsrover.R;
import com.android.msahakyan.marsrover.model.Rover;
import com.android.msahakyan.marsrover.util.AnimationUtils;
import com.android.msahakyan.marsrover.util.OnRoverSelectedListener;
import com.android.msahakyan.marsrover.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpeedDialFragment extends DialogFragment {

    private static final String TAG = SpeedDialFragment.class.getSimpleName();
    private static List<Rover> mRovers;
    private OnRoverSelectedListener mListener;

    @Bind(R.id.speed_dial_button_container)
    RelativeLayout mContentContainer;

    @Bind(R.id.item_container)
    LinearLayout mItemContainer;

    @Bind(R.id.close_action_button)
    FloatingActionButton mCloseActionButton;

    public static void show(FragmentManager fm, List<Rover> rovers) {
        new SpeedDialFragment().show(fm, "speed_dial_fragment");
        mRovers = rovers;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.Translucent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_speed_dial, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mContentContainer.setOnClickListener(v -> hide());
        setUp();
        show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnRoverSelectedListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setUp() {

        for (Rover rover : mRovers) {
            SpeedDialElementView speedDialElementView = new SpeedDialElementView(getActivity());
            speedDialElementView.mLabelView.setText(rover.getName());
            speedDialElementView.mLabelView.setTextColor(getResources().getColor(R.color.colorAccent));
            speedDialElementView.mActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentLight)));
            speedDialElementView.mActionButton.setImageResource(Utils.getRoverThumbId(rover.getName()));
            speedDialElementView.setOnClickListener(new OnCreateRoverClickListener(rover));

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.bottomMargin = (int) getResources().getDimension(R.dimen.speed_dial_row_margin);
            layoutParams.gravity = Gravity.END;

            speedDialElementView.setLayoutParams(layoutParams);

            mItemContainer.addView(speedDialElementView);
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.close_action_button)
    void onCloseClick() {
        hide();
    }


    private void show() {
        mContentContainer.setVisibility(View.VISIBLE);
        mCloseActionButton.post(() -> playAnimation(true, null));
    }

    private void hide() {
        mCloseActionButton.post(() -> playAnimation(false, this::dismissIfShown));
    }

    private void dismissIfShown() {
        if (getActivity() != null && getDialog() != null) {
            dismissAllowingStateLoss();
        }
    }

    private void playAnimation(final boolean show, final OnAnimationFinishedListener listener) {

        if (getView() == null) {
            Log.d(TAG, "The view has been destroyed");
            return;
        }

        final RotateDrawable rotateDrawable = (RotateDrawable) mCloseActionButton.getDrawable();
        Animator animator = AnimationUtils.rotateDrawable(rotateDrawable, !show);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                int anchorY = mCloseActionButton.getHeight();
                for (SpeedDialElementView element : getChildren(mItemContainer)) {
                    if (show) {
                        element.show(anchorY);
                    } else {
                        element.hide(anchorY);
                    }
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null) {
                    listener.onAnimationFinished();
                }
            }
        });

        animator.start();
    }

    private List<SpeedDialElementView> getChildren(ViewGroup parent) {
        List<SpeedDialElementView> children = new ArrayList<>(mRovers.size());
        for (int i = 0; i < parent.getChildCount(); i++) {
            children.add((SpeedDialElementView) parent.getChildAt(i));
        }
        return children;
    }

    @Override
    public void onDestroyView() {
        mItemContainer = null;
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class OnCreateRoverClickListener implements View.OnClickListener {
        private Rover mRover;

        public OnCreateRoverClickListener(Rover rover) {
            mRover = rover;
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onRoverSelected(mRover);
            }
            dismissIfShown();
        }
    }

    public interface OnAnimationFinishedListener {
        void onAnimationFinished();
    }
}
