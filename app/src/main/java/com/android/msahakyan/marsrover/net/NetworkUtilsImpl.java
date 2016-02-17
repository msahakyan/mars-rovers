package com.android.msahakyan.marsrover.net;

import com.android.msahakyan.marsrover.application.AppController;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Map;

/**
 * @author msahakyan
 */
public class NetworkUtilsImpl implements INetworkUtils<JSONObject> {

    private static final int REQUEST_TIMEOUT = 5 * 1000;
    private static final int MAX_RETRIES = 3;
    private static final int BACKOFF_MULTIPLIER = 1;

    private String mTag;

    public NetworkUtilsImpl(String tag) {
        mTag = tag;
    }

    @Override
    public void executeNetworkRequest(int method, StringBuilder endpoint,
                                      Map<String, String> urlParams, final NetworkRequestListener<JSONObject> requestListener) {

        if (urlParams != null) {
            int i = 0;
            for (Map.Entry<String, String> entry : urlParams.entrySet()) {
                if (i == 0) {
                    endpoint.append("?");
                } else {
                    endpoint.append("&");
                }
                endpoint.append(entry.getKey()).append("=").append(entry.getValue());
                i++;
            }
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, endpoint.toString(), null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    requestListener.onSuccess(response);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    requestListener.onError(error);
                }
            });

        jsonObjectRequest.setRetryPolicy(new CustomRetryPolicy(REQUEST_TIMEOUT, MAX_RETRIES, BACKOFF_MULTIPLIER));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, mTag);
    }
}
