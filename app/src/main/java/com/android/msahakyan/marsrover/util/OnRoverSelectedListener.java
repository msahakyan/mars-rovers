package com.android.msahakyan.marsrover.util;

import com.android.msahakyan.marsrover.model.Rover;

/**
 * @author msahakyan
 */
public interface OnRoverSelectedListener {

    /**
     * Callback method for rover selection
     *
     * @param rover selected rover
     */
    void onRoverSelected(Rover rover);
}
