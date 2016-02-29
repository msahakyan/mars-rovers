package com.android.msahakyan.marsrover.loader;

import com.android.msahakyan.marsrover.model.RoverType;

import java.util.ArrayList;
import java.util.List;

public class LoaderStateManager /*implements MainActivity.OnDataLoaderChangeListener*/ {

    private static final int LOADER_ID_SPIRIT = 100;
    private static final int LOADER_ID_OPPORTUNITY = 101;
    private static final int LOADER_ID_CURIOSITY = 102;

    private List<LoaderState> mLoaderStates;

    private static LoaderStateManager sInstance;

    public static LoaderStateManager getInstance() {
        if (sInstance == null) {
            sInstance = new LoaderStateManager();
        }
        return sInstance;
    }

    private LoaderStateManager() {
        mLoaderStates = new ArrayList<>();
        mLoaderStates.add(createEntryStateForLoader(LOADER_ID_SPIRIT, RoverType.SPIRIT));
        mLoaderStates.add(createEntryStateForLoader(LOADER_ID_OPPORTUNITY, RoverType.OPPORTUNITY));
        mLoaderStates.add(createEntryStateForLoader(LOADER_ID_CURIOSITY, RoverType.CURIOSITY));
    }

    private LoaderState createEntryStateForLoader(int loaderId, RoverType roverType) {
        return new LoaderState(loaderId, roverType, true);
    }

    public boolean isLoaderActive(int loaderId) {
        for (LoaderState loaderState : mLoaderStates) {
            if (loaderState.getLoaderId() == loaderId && loaderState.isActive()) {
                return true;
            }
        }
        return false;
    }

    public void setLoaderActiveState(int loaderId, boolean isActive) {
        for (LoaderState loaderState : mLoaderStates) {
            if (loaderState.getLoaderId() == loaderId) {
                loaderState.setActive(isActive);
            }
        }
    }

    public boolean hasActiveLoaders() {
        for (LoaderState loaderState : mLoaderStates) {
            if (loaderState.isActive()) {
                return true;
            }
        }
        return false;
    }

    public void activateAllLoaders() {
        for (LoaderState loaderState : mLoaderStates) {
            loaderState.setActive(true);
        }
    }

    public void deactivateAllLoaders() {
        for (LoaderState loaderState : mLoaderStates) {
            loaderState.setActive(false);
        }
    }

    /**
     * Deactivates other loaders besides given one
     * @param roverType RoverType of the loader which should remain activated
     */
    public void deactivateOtherLoaders(RoverType roverType) {
        for (LoaderState loaderState : mLoaderStates) {
            if (!loaderState.getRoverType().equals(roverType)) {
                loaderState.setActive(false);
            }
        }
    }

    public List<Integer> getActiveLoaderIds() {
        List<Integer> activeLoaderIds = new ArrayList<>(mLoaderStates.size());

        for (LoaderState state : mLoaderStates) {
            if (state.isActive()) {
                activeLoaderIds.add(state.getLoaderId());
            }
        }
        return activeLoaderIds;
    }

    public List<LoaderState> getLoaderStates() {
        return mLoaderStates;
    }

    /**
     * Helper class which keeps loader `id` and `state (activated/deactivated)
     */
    public final class LoaderState {

        private int mLoaderId;
        private boolean mIsActive;
        private RoverType mRoverType;

        LoaderState() {
        }

        LoaderState(int loaderId, RoverType roverType, boolean isActive) {
            mLoaderId = loaderId;
            mIsActive = isActive;
            mRoverType = roverType;
        }

        public int getLoaderId() {
            return mLoaderId;
        }

        public void setLoaderId(int loaderId) {
            mLoaderId = loaderId;
        }

        public boolean isActive() {
            return mIsActive;
        }

        public void setActive(boolean active) {
            mIsActive = active;
        }

        public RoverType getRoverType() {
            return mRoverType;
        }

        public void setRoverType(RoverType roverType) {
            mRoverType = roverType;
        }
    }
}
