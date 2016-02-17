package com.android.msahakyan.marsrover.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author msahakyan
 */
public class Rover implements Parcelable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("landing_date")
    private String landingDate;

    @SerializedName("max_sol")
    private int maxSol;

    @SerializedName("max_date")
    private String maxDate;

    @SerializedName("total_photos")
    private int totalPhotos;

    @SerializedName("cameras")
    private List<Camera> cameras;

    protected Rover(Parcel in) {
        id = in.readInt();
        name = in.readString();
        landingDate = in.readString();
        maxSol = in.readInt();
        maxDate = in.readString();
        totalPhotos = in.readInt();
        cameras = in.createTypedArrayList(Camera.CREATOR);
    }

    public static final Creator<Rover> CREATOR = new Creator<Rover>() {
        @Override
        public Rover createFromParcel(Parcel in) {
            return new Rover(in);
        }

        @Override
        public Rover[] newArray(int size) {
            return new Rover[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLandingDate() {
        return landingDate;
    }

    public void setLandingDate(String landingDate) {
        this.landingDate = landingDate;
    }

    public int getMaxSol() {
        return maxSol;
    }

    public void setMaxSol(int maxSol) {
        this.maxSol = maxSol;
    }

    public String getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(String maxDate) {
        this.maxDate = maxDate;
    }

    public int getTotalPhotos() {
        return totalPhotos;
    }

    public void setTotalPhotos(int totalPhotos) {
        this.totalPhotos = totalPhotos;
    }

    public List<Camera> getCameras() {
        return cameras;
    }

    public void setCameras(List<Camera> cameras) {
        this.cameras = cameras;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(landingDate);
        dest.writeInt(maxSol);
        dest.writeString(maxDate);
        dest.writeInt(totalPhotos);
        dest.writeTypedList(cameras);
    }
}
