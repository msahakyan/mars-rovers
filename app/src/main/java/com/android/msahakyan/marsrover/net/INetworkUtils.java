package com.android.msahakyan.marsrover.net;

import java.util.Map;

/**
 * @author msahakyan
 */
public interface INetworkUtils<D> {

    /**
     * Creates JsonArrayRequest
     *
     * @param method
     * @param endpoint
     * @param urlParams
     * @param requestListener
     */
    void executeNetworkRequest(int method, StringBuilder endpoint, Map<String, String> urlParams,
                               final NetworkRequestListener<D> requestListener);
}
