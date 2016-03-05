package com.android.msahakyan.marsrover.util;

import com.android.volley.VolleyError;

/**
 * @author msahakyan
 *         Notifies about data loading process error
 */
public interface OnLoadErrorListener {

    /**
     * Callback method for notifying about loading error
     *
     * @param error    exception which has been thrown
     * @param loaderId id of a loader
     */
    void onLoadError(VolleyError error, int loaderId);
}
