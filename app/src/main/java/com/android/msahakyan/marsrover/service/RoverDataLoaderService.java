package com.android.msahakyan.marsrover.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.msahakyan.marsrover.model.NetworkRequest;
import com.android.msahakyan.marsrover.model.Rover;
import com.android.msahakyan.marsrover.net.NetHelper;
import com.android.msahakyan.marsrover.net.NetworkRequestListener;
import com.android.msahakyan.marsrover.net.NetworkUtilsImpl;
import com.android.msahakyan.marsrover.net.parser.RoverListParser;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

/**
 * @author msahakyan
 *         An {@link IntentService} subclass for handling asynchronous task requests in
 *         a service on a separate handler thread.
 */
public class RoverDataLoaderService extends IntentService {

    private static final String TAG = RoverDataLoaderService.class.getSimpleName();
    private static final boolean DEBUG = true;

    public RoverDataLoaderService() {
        super("RoverDataLoaderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (DEBUG) Log.i(TAG, "#### Starting RoverLoaderDataService ####");
        NetworkRequest request = NetHelper.composeRequestForRovers();
        new NetworkUtilsImpl(TAG).executeNetworkRequest(request.getMethod(), new StringBuilder(request.getEndpoint()),
            request.getUrlParams(), new NetworkRequestListener<JSONObject>() {
                @Override
                public void onSuccess(JSONObject data) {
                    if (DEBUG) Log.i(TAG, "#### RoverDataLoadService finished successfully ####");
                    List<Rover> rovers = new Gson().fromJson(data.toString(), RoverListParser.class).getRoverList();
                    publishRoverListData(rovers);
                }

                @Override
                public void onError(VolleyError error) {
                    if (DEBUG) Log.w(TAG, "#### RoverDataLoadService failed! ####");
                    Log.e(TAG, "#### Network response code: " + (error != null ? error.toString() : "Could not load rovers data") + " ####");
                    Toast.makeText(RoverDataLoaderService.this, "Could not load rovers data", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void publishRoverListData(List<Rover> rovers) {
        LoaderDataSource.getInstance().setRoverList(rovers);
    }
}
