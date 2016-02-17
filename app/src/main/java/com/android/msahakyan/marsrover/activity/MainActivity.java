package com.android.msahakyan.marsrover.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.msahakyan.marsrover.R;
import com.android.msahakyan.marsrover.adapter.RoverDataAdapter;
import com.android.msahakyan.marsrover.loader.DataLoader;
import com.android.msahakyan.marsrover.model.NetworkRequest;
import com.android.msahakyan.marsrover.model.Photo;
import com.android.msahakyan.marsrover.model.RoverType;
import com.android.msahakyan.marsrover.net.Endpoint;
import com.android.msahakyan.marsrover.net.parser.PhotoListParser;
import com.android.msahakyan.marsrover.util.Config;
import com.android.msahakyan.marsrover.util.PhotoItemDecorator;
import com.android.volley.Request;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private static final int LOADER_ID_SPIRIT = 100;
    private static final int LOADER_ID_OPPORTUNITY = 101;
    private static final int LOADER_ID_CURIOSITY = 102;

    private List<Photo> mPhotos;
    private GridLayoutManager mLayoutManager;
    private int mSol = 1;

    @Bind(R.id.photos_recycler_view)
    protected RecyclerView mRecyclerView;

    @Bind(R.id.progress_view)
    protected CircularProgressView mProgressView;

    private RoverDataAdapter mAdapter;

    private boolean isSpiritRequestOngoing = false;
    private boolean isOpportunityRequestOngoing = false;
    private boolean isCuriosityRequestOngoing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mPhotos = new ArrayList<>();

        mLayoutManager = new GridLayoutManager(this, 2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });

        mAdapter = new RoverDataAdapter(MainActivity.this, mPhotos);
        mRecyclerView.setAdapter(mAdapter);

        // Set layout manager to recyclerView
        mRecyclerView.setLayoutManager(mLayoutManager);
        int space = (int) getResources().getDimension(R.dimen.margin_size_medium);

        mRecyclerView.addItemDecoration(new PhotoItemDecorator(space, space));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    loadMoreItems();
                }
            }
        });

        showProgressView(false);

        getSupportLoaderManager().initLoader(LOADER_ID_SPIRIT, null, this);
        getSupportLoaderManager().initLoader(LOADER_ID_OPPORTUNITY, null, this);
        getSupportLoaderManager().initLoader(LOADER_ID_CURIOSITY, null, this);
    }

    private NetworkRequest createRequest(RoverType roverType, int sol, @Nullable String camera) {
        NetworkRequest request = new NetworkRequest();

        String endpoint = Endpoint.NASA_ENDPOINT + RoverType.getName(roverType.ordinal()) + Endpoint.PHOTOS;
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("api_key", Config.API_KEY);
        urlParams.put("sol", String.valueOf(sol));
        if (camera != null) {
            urlParams.put("camera", camera);
        }

        request.setMethod(Request.Method.GET);
        request.setEndpoint(endpoint);
        request.setUrlParams(urlParams);

        return request;
    }

    private void loadMoreItems() {
        if ((isSpiritRequestOngoing && isOpportunityRequestOngoing && isCuriosityRequestOngoing)) {
            return;
        }

        int visibleItemCount = mLayoutManager.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
            // Increase sol value with 1
            mSol++;

            Timber.d("Loading more data for sol: " + mSol);
            showProgressView(true);

            // [Spirit data content change -- start]
            Timber.d("Loading more data for Spirit-Rover sol: " + mSol);
            NetworkRequest networkRequestSpirit = createRequest(RoverType.SPIRIT, mSol, null);
            DataLoader spiritLoader = (DataLoader) getSupportLoaderManager().getLoader(LOADER_ID_SPIRIT);
            spiritLoader.setNetworkRequest(networkRequestSpirit);
            spiritLoader.onContentChanged();
            isSpiritRequestOngoing = true;
            // [Spirit data content change -- end]

            // [Opportunity data content change -- start]
            Timber.d("Loading more data for Opportunity-Rover sol: " + mSol);
            NetworkRequest networkRequestOpportunity = createRequest(RoverType.OPPORTUNITY, mSol, null);
            DataLoader opportunityLoader = (DataLoader) getSupportLoaderManager().getLoader(LOADER_ID_OPPORTUNITY);
            opportunityLoader.setNetworkRequest(networkRequestOpportunity);
            opportunityLoader.onContentChanged();
            isOpportunityRequestOngoing = true;
            // [Opportunity data content change -- end]

            // [Curiosity data content change -- start]
            Timber.d("Loading more data for Curiosity-Rover sol: " + mSol);
            NetworkRequest networkRequestCuriosity = createRequest(RoverType.CURIOSITY, mSol, null);
            DataLoader curiosityLoader = (DataLoader) getSupportLoaderManager().getLoader(LOADER_ID_CURIOSITY);
            curiosityLoader.setNetworkRequest(networkRequestCuriosity);
            curiosityLoader.onContentChanged();
            isCuriosityRequestOngoing = true;
            // [Curiosity data content change -- end]
        }
    }

    @Override
    public Loader<JSONObject> onCreateLoader(int id, Bundle args) {
        NetworkRequest request;

        switch (id) {
            case LOADER_ID_SPIRIT:
                request = createRequest(RoverType.SPIRIT, mSol, null);
                isSpiritRequestOngoing = true;
                break;
            case LOADER_ID_OPPORTUNITY:
                request = createRequest(RoverType.OPPORTUNITY, mSol, null);
                isOpportunityRequestOngoing = true;
                break;
            case LOADER_ID_CURIOSITY:
                request = createRequest(RoverType.CURIOSITY, mSol, null);
                break;
            default:
                throw new IllegalArgumentException("Not supported rover type");
        }
        return new DataLoader<>(getApplicationContext(), request);
    }

    @Override
    public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {
        switch (loader.getId()) {
            case LOADER_ID_SPIRIT:
                isSpiritRequestOngoing = false;
                break;
            case LOADER_ID_OPPORTUNITY:
                isOpportunityRequestOngoing = false;
                break;
            case LOADER_ID_CURIOSITY:
                isCuriosityRequestOngoing = false;
                break;
            default:
                break;
        }

        List<Photo> photos = new Gson().fromJson(data.toString(), PhotoListParser.class).getPhotoList();
        mPhotos.addAll(photos);
        int oldAdapterSize = mAdapter.getItemCount();
        mAdapter.notifyItemRangeInserted(oldAdapterSize, photos.size());

        hideProgressView();
        Timber.d("All rovers data loaded successfully. Sol: " + mSol);
    }


    @Override
    public void onLoaderReset(Loader<JSONObject> loader) {

        loader.reset();
        mAdapter = null;
        isSpiritRequestOngoing = false;
        isOpportunityRequestOngoing = false;
        isCuriosityRequestOngoing = false;
    }

    private void hideProgressView() {
        if (mProgressView != null) {
            mProgressView.setVisibility(View.GONE);
            mProgressView.clearAnimation();
        }
    }

    private void showProgressView(boolean bottom) {
        if (mProgressView != null) {
            if (bottom) {
                mProgressView.setBottom(0);
            }
            mProgressView.setVisibility(View.VISIBLE);
            mProgressView.startAnimation();
        }
    }
}
