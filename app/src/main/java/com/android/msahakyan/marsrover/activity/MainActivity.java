package com.android.msahakyan.marsrover.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.msahakyan.marsrover.R;
import com.android.msahakyan.marsrover.adapter.RoverDataAdapter;
import com.android.msahakyan.marsrover.fragment.SelectRoverFragment;
import com.android.msahakyan.marsrover.loader.DataLoader;
import com.android.msahakyan.marsrover.loader.LoaderStateManager;
import com.android.msahakyan.marsrover.model.NetworkRequest;
import com.android.msahakyan.marsrover.model.Photo;
import com.android.msahakyan.marsrover.model.Rover;
import com.android.msahakyan.marsrover.model.RoverType;
import com.android.msahakyan.marsrover.net.NetHelper;
import com.android.msahakyan.marsrover.net.parser.PhotoListParser;
import com.android.msahakyan.marsrover.service.LoaderDataSource;
import com.android.msahakyan.marsrover.speedDial.SpeedDialFragment;
import com.android.msahakyan.marsrover.util.BundleKey;
import com.android.msahakyan.marsrover.util.ItemClickListener;
import com.android.msahakyan.marsrover.util.OnLoadErrorListener;
import com.android.msahakyan.marsrover.util.OnRoverSelectedListener;
import com.android.msahakyan.marsrover.util.PhotoItemDecorator;
import com.android.volley.VolleyError;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements
    LoaderManager.LoaderCallbacks<JSONObject>, OnRoverSelectedListener, OnLoadErrorListener, ItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final boolean DEBUG = true;
    private static final int REQUEST_DETAIL_CONTENT = 333;

    private LoaderStateManager mLoaderStateManager;

    private List<Photo> mPhotos;
    private GridLayoutManager mLayoutManager;
    private int mSol = 1;

    @Bind(R.id.photos_recycler_view)
    protected RecyclerView mRecyclerView;

    @Bind(R.id.progress_view)
    protected CircularProgressView mProgressView;

    private RoverDataAdapter mAdapter;

    private int mLoadingState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mLoaderStateManager = LoaderStateManager.getInstance();
        mPhotos = new ArrayList<>();

        mLayoutManager = new GridLayoutManager(this, 2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });

        mAdapter = new RoverDataAdapter(this, mPhotos);
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

        for (int loaderId : mLoaderStateManager.getActiveLoaderIds()) {
            getSupportLoaderManager().initLoader(loaderId, null, this);
        }
    }

    private void loadMoreItems() {
        if (mLoadingState > 0) {
            if (DEBUG) Log.d(TAG, "There is still some ongoing load process -- skip");
            return;
        }

        int visibleItemCount = mLayoutManager.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
            // Increase sol value by one
            mSol++;

            if (DEBUG) Log.d(TAG, "Loading more data for sol: " + mSol);
            showProgressView(true);

            for (LoaderStateManager.LoaderState state : mLoaderStateManager.getLoaderStates()) {
                if (state.isActive()) {
                    notifyLoaderDataChanged(state.getLoaderId(), state.getRoverType());
                } else {
                    if (DEBUG)
                        Log.i(TAG, "Skip deactivated Loader: " + state.getRoverType().name().toLowerCase());
                }
            }
        }
    }

    private void notifyLoaderDataChanged(int loaderId, RoverType roverType) {
        if (DEBUG)
            Log.d(TAG, "Loading data for loader: " + roverType.name() + ", sol: " + mSol);
        NetworkRequest networkRequest = NetHelper.composeRequestForSol(roverType, mSol, null);
        DataLoader dataLoader = (DataLoader) getSupportLoaderManager().getLoader(loaderId);
        dataLoader.setNetworkRequest(networkRequest);
        dataLoader.onContentChanged();
        showProgressView(false);
        mLoadingState++;
    }

    @Override
    public Loader<JSONObject> onCreateLoader(int id, Bundle args) {
        NetworkRequest request = null;

        for (LoaderStateManager.LoaderState state : mLoaderStateManager.getLoaderStates()) {
            if (id == state.getLoaderId()) {
                request = NetHelper.composeRequestForSol(state.getRoverType(), mSol, null);
                mLoadingState++;
                break;
            }
        }

        return new DataLoader<>(getApplicationContext(), request, this);
    }

    @Override
    public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {

        for (LoaderStateManager.LoaderState state : mLoaderStateManager.getLoaderStates()) {
            if (loader.getId() == state.getLoaderId()) {
                if (DEBUG)
                    Log.d(TAG, "Data for loader: " + loader.getId() + " loaded successfully. Sol: " + mSol);
                mLoadingState--;
                break;
            }
        }

        List<Photo> photos = new Gson().fromJson(data.toString(), PhotoListParser.class).getPhotoList();
        mPhotos.addAll(photos);
        int oldAdapterSize = mAdapter.getItemCount();
        mAdapter.notifyItemRangeInserted(oldAdapterSize, photos.size());

        hideProgressView();

        if (mLoadingState == 0) {
            if (DEBUG) Log.d(TAG, "All rovers data loaded successfully. Sol: " + mSol);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
//            case R.id.menu_calendar:
//                final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        // TODO Implement loading data by selected earth-date
//                    }
//                }, 0, 0, 0);
//                datePickerDialog.show();
//                break;
            case R.id.menu_rover:
                Fragment fragment = SelectRoverFragment.newInstance();
                getSupportFragmentManager().beginTransaction().add(fragment, "TAG").commit();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoaderReset(Loader<JSONObject> loader) {
        loader.reset();
        mAdapter = null;
        mLoadingState = 0;
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

    @OnClick(R.id.fab_rovers)
    @SuppressWarnings("unused")
    void onActionButtonClick() {
        SpeedDialFragment.show(getSupportFragmentManager(), LoaderDataSource.getInstance().getRoverList());
    }

    @Override
    public void onRoverSelected(Rover rover) {

        // Clear previously loaded data
        mSol = 1;
        mPhotos.clear();
        mAdapter.clearData();

        RoverType selectedRoverType = RoverType.getTypeByName(rover.getName());

        int loaderId = getLoaderIdByRoverType(selectedRoverType);

        // Activate selected loader
        mLoaderStateManager.setLoaderActiveState(loaderId, true);

        // Deactivate other loader states
        mLoaderStateManager.deactivateOtherLoaders(selectedRoverType);

        // Notify loader manager that selected loader data has been changed
        notifyLoaderDataChanged(loaderId, selectedRoverType);
    }

    private int getLoaderIdByRoverType(RoverType roverType) {
        for (LoaderStateManager.LoaderState state : mLoaderStateManager.getLoaderStates()) {
            if (state.getRoverType() == roverType) {
                return state.getLoaderId();
            }
        }
        return -1;
    }

    private RoverType getRoverTypeByLoaderId(int loaderId) {
        for (LoaderStateManager.LoaderState state : mLoaderStateManager.getLoaderStates()) {
            if (state.getLoaderId() == loaderId) {
                return state.getRoverType();
            }
        }
        return null;
    }

    @Override
    public void onLoadError(VolleyError error, int loaderId) {
        if (error != null) {
            mLoadingState--;
            if (error.networkResponse != null) {
                switch (error.networkResponse.statusCode) {
                    case 400:
                        if (mLoaderStateManager.getActiveLoaderIds().size() == 1) {
                            hideProgressView();
                            mSol++;
                            // Notify loader manager that selected loader data has been changed
                            notifyLoaderDataChanged(loaderId, getRoverTypeByLoaderId(loaderId));
                        } else {
                            Log.i(TAG, "Skip empty list of photos for Rover: " + getRoverTypeByLoaderId(loaderId) + " for sol: " + mSol);
                        }
                        break;
                    // handle other errors here
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void onClick(View view, int position) {
        startDetailPagerActivity(position);
    }

    private void startDetailPagerActivity(int position) {
        List<Photo> photosToBeSend = new ArrayList<>(mPhotos.subList(position, mPhotos.size()));
        Intent intent = new Intent(this, DetailPagerActivity.class);
        intent.putExtra(BundleKey.EXTRA_START_POSITION, position);
        intent.putParcelableArrayListExtra(BundleKey.EXTRA_PHOTOS_TO_BE_SEND, (ArrayList<? extends Parcelable>) photosToBeSend);
        this.startActivityForResult(intent, REQUEST_DETAIL_CONTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_DETAIL_CONTENT:
                if (resultCode == RESULT_OK) {
                    int position = data.getIntExtra(BundleKey.EXTRA_POSITION, 0);
                    if (mRecyclerView != null) {
                        Log.d(TAG, "Scrolling recycler view to position: " + position);
                        mRecyclerView.post(() -> mRecyclerView.smoothScrollToPosition(position));
                    }
                } else {
                    Log.w(TAG, "Something was going wrong -- result code is not RESULT_OK");
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
