package com.bignerdranch.android.photogallery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Jack on 2/17/2018.
 */

public class GalleryItem {

    @SerializedName("title")
    private String mTitle;

    @SerializedName("id")
    private String mId;

    @SerializedName("url_s")
    private String mUrl;

    @Override
    public String toString() {
        return mTitle;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

}
