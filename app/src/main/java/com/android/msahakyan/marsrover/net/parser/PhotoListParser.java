package com.android.msahakyan.marsrover.net.parser;

import com.android.msahakyan.marsrover.model.Photo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author msahakyan
 */
public class PhotoListParser {

    @SerializedName("photos")
    private List<Photo> photoList;

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }
}
