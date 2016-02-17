package com.android.msahakyan.marsrover.net;

import com.android.volley.VolleyError;

/**
 * @author msahakyan
 */
public interface NetworkRequestListener<D> {

    /**
     * Success callback
     *
     * @param data
     */
    void onSuccess(D data);

    /**
     * Error callback
     *
     * @param error
     */
    void onError(VolleyError error);
}
