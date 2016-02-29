package com.android.msahakyan.marsrover.util;

import com.android.msahakyan.marsrover.R;
import com.android.msahakyan.marsrover.model.RoverType;

/**
 * @author msahakyan
 */
public class Utils {

    public static int getRoverThumbId(String roverName) {
        if (RoverType.CURIOSITY.name().equalsIgnoreCase(roverName)) {
            return R.drawable.rover_curiosity_icon;
        } else if (RoverType.SPIRIT.name().equalsIgnoreCase(roverName)) {
            return R.drawable.rover_spirit_icon;
        } else if (RoverType.OPPORTUNITY.name().equalsIgnoreCase(roverName)) {
            return R.drawable.rover_opportunity_icon;
        } else {
            throw new IllegalArgumentException("Not supported rover type");
        }
    }
}
