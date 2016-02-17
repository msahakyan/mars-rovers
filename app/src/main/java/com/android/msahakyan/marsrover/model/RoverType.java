package com.android.msahakyan.marsrover.model;

/**
 * @author msahakyan
 */
public enum RoverType {

    CURIOSITY(1, "curiosity"),
    OPPORTUNITY(2, "opportunity"),
    SPIRIT(3, "spirit");

    RoverType(int id, String name) {
    }

    public static String getName(int id) {
        for (RoverType rover : values()) {
            if (rover.ordinal() == id) {
                return rover.name();
            }
        }
        throw new IllegalArgumentException("Rover with id[" + id + "] does not exists");
    }
}
