package com.android.msahakyan.marsrover.service;

import com.android.msahakyan.marsrover.model.Rover;

import java.util.List;

/**
 * @author msahakyan
 *         <p/>
 *         RoverDataLoaderService will load rovers data when app starts and will
 *         publish corresponding data to this class
 */
public class LoaderDataSource {

    private LoaderDataSource() {
    }

    private static LoaderDataSource sInstance;
    private List<Rover> mRoverList;

    public static LoaderDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new LoaderDataSource();
        }
        return sInstance;
    }


    public List<Rover> getRoverList() {
        return mRoverList;
    }

    void setRoverList(List<Rover> roverList) {
        this.mRoverList = roverList;
    }
}
