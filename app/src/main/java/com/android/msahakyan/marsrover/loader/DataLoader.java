package com.android.msahakyan.marsrover.loader;

import android.content.Context;
import android.support.v4.content.Loader;
import android.util.Log;

import com.android.msahakyan.marsrover.activity.MainActivity;
import com.android.msahakyan.marsrover.application.AppController;
import com.android.msahakyan.marsrover.model.NetworkRequest;
import com.android.msahakyan.marsrover.net.NetworkRequestListener;
import com.android.msahakyan.marsrover.net.NetworkUtilsImpl;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * @author msahakyan
 */
public class DataLoader<D extends JSONObject> extends Loader<D> {

    public static final String TAG = DataLoader.class.getSimpleName();
    public static final boolean DEBUG = true;

    private D mCachedData;
    private NetworkRequest mRequest;
    private RequestQueue mRequestQueue;

    public DataLoader(Context context, NetworkRequest request) {
        super(context);
        mRequestQueue = AppController.getInstance().getRequestQueue();
        mRequest = request;
    }

    @Override
    protected void onStartLoading() {
        if (mCachedData != null) {
            // Deliver any previously loaded data immediately.
            if (DEBUG) Log.i(TAG, "Delivering previously loaded data to the client...");
            deliverResult(mCachedData);
        }

        if (takeContentChanged()) {
            if (DEBUG) Log.i(TAG, "#### A content change has been detected... so force load! ####");
            forceLoad();
        } else if (mCachedData == null) {
            // If the current data is null... then we should try to load new data
            if (DEBUG) Log.i(TAG, "#### The current data is null... so force load! ####");
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        if (DEBUG) Log.i(TAG, "#### onStopLoading() called! ####");
        if (isStarted()) {
            reset();
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        if (DEBUG) Log.i(TAG, "#### forceLoad() called! ####");
        new NetworkUtilsImpl(TAG).executeNetworkRequest(mRequest.getMethod(), new StringBuilder(mRequest.getEndpoint()),
            mRequest.getUrlParams(), new NetworkRequestListener<JSONObject>() {
                @Override
                public void onSuccess(JSONObject data) {
                    deliverResult((D) data);
                }

                @Override
                public void onError(VolleyError error) {
                    Log.e(TAG, error != null ? error.toString() : "Could not force load");
                    MainActivity.decreaseLoadingState();
                }
            });
    }

    @Override
    protected void onReset() {
        super.onReset();
        if (DEBUG) Log.i(TAG, "#### onReset() called! ####");

        // Ensure the loader is stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'apps'.
        if (mCachedData != null) {
            releaseResources(mCachedData);
            mCachedData = null;
        }

        mRequestQueue.cancelAll(TAG);
    }

    @Override
    protected boolean onCancelLoad() {
        // The load has been canceled, so we should release the resources
        if (DEBUG) Log.i(TAG, "#### onCanceled() called! ####");
        releaseResources(mCachedData);

        return super.onCancelLoad();
    }

    @Override
    public void deliverResult(D data) {
        if (DEBUG) Log.i(TAG, "#### deliverResult() called! ####");
        if (isReset()) {
            if (data != null) {
                releaseResources(data);
                return;
            }
        }

        mCachedData = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    private void releaseResources(D data) {
        // Here we should release our resources
        // For a simple List, there is nothing to do.
    }

    public void setNetworkRequest(NetworkRequest request) {
        mRequest = request;
    }


}
