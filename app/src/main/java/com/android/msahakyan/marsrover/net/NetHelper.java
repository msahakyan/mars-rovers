package com.android.msahakyan.marsrover.net;

import com.android.msahakyan.marsrover.model.NetworkRequest;
import com.android.msahakyan.marsrover.model.RoverType;
import com.android.msahakyan.marsrover.util.Config;
import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * @author msahakyan
 */
public class NetHelper {

    private static NetworkRequest createNetworkRequestForPhotos(RoverType roverType, Map<String, String> urlParams) {
        NetworkRequest request = new NetworkRequest();
        request.setMethod(Request.Method.GET);
        request.setEndpoint(Endpoint.NASA_ENDPOINT + "/" + RoverType.getName(roverType.ordinal()) + Endpoint.PHOTOS);
        request.setUrlParams(urlParams);

        return request;
    }

    private static NetworkRequest createNetworkRequestForRovers(Map<String, String> urlParams) {
        NetworkRequest request = new NetworkRequest();
        request.setMethod(Request.Method.GET);
        request.setEndpoint(Endpoint.NASA_ENDPOINT);
        request.setUrlParams(urlParams);

        return request;
    }

    private static NetworkRequest createNetworkRequestForRover(RoverType roverType, Map<String, String> urlParams) {
        NetworkRequest request = new NetworkRequest();
        request.setMethod(Request.Method.GET);
        request.setEndpoint(Endpoint.NASA_ENDPOINT + "/" + RoverType.getName(roverType.ordinal()));
        request.setUrlParams(urlParams);

        return request;
    }

    public static NetworkRequest composeRequestForSol(RoverType roverType, int sol, String camera) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("api_key", Config.API_KEY);
        urlParams.put("sol", String.valueOf(sol));
        if (camera != null) {
            urlParams.put("camera", camera);
        }

        return createNetworkRequestForPhotos(roverType, urlParams);
    }

    public static NetworkRequest composeRequestForEarthDate(RoverType roverType, String earthDate, String camera) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("api_key", Config.API_KEY);
        urlParams.put("earth_date", earthDate);
        if (camera != null) {
            urlParams.put("camera", camera);
        }

        return createNetworkRequestForPhotos(roverType, urlParams);
    }

    public static NetworkRequest composeRequestForRovers() {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("api_key", Config.API_KEY);

        return createNetworkRequestForRovers(urlParams);
    }

    public static NetworkRequest composeRequestForRover(RoverType roverType) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("api_key", Config.API_KEY);

        return createNetworkRequestForRover(roverType, urlParams);
    }
}
