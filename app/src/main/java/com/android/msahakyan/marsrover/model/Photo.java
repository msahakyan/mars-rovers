package com.android.msahakyan.marsrover.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author msahakyan
 */
public class Photo implements Parcelable {

    @SerializedName("id")
    private int id;

    @SerializedName("sol")
    private int sol;

    @SerializedName("camera")
    private Camera camera;

    @SerializedName("img_src")
    private String imgSrc;

    @SerializedName("earth_date")
    private String earthDate;

    @SerializedName("rover")
    private Rover rover;

    protected Photo(Parcel in) {
        id = in.readInt();
        sol = in.readInt();
        camera = in.readParcelable(Camera.class.getClassLoader());
        imgSrc = in.readString();
        earthDate = in.readString();
        rover = in.readParcelable(Rover.class.getClassLoader());
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSol() {
        return sol;
    }

    public void setSol(int sol) {
        this.sol = sol;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getEarthDate() {
        return earthDate;
    }

    public void setEarthDate(String earthDate) {
        this.earthDate = earthDate;
    }

    public Rover getRover() {
        return rover;
    }

    public void setRover(Rover rover) {
        this.rover = rover;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(sol);
        dest.writeParcelable(camera, flags);
        dest.writeString(imgSrc);
        dest.writeString(earthDate);
        dest.writeParcelable(rover, flags);
    }
}
