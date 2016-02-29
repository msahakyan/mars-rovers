package com.android.msahakyan.marsrover.net.parser;

import com.android.msahakyan.marsrover.model.Rover;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author msahakyan
 */
public class RoverListParser {

    @SerializedName("rovers")
    private List<Rover> roverList;

    public List<Rover> getRoverList() {
        return roverList;
    }

    public void setRoverList(List<Rover> roverList) {
        this.roverList = roverList;
    }
}
