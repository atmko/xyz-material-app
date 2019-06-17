/*
 * Copyright (C) 2019 Aayat Mimiko
 */

package com.example.xyzreader.models;

import org.parceler.Parcel;

@Parcel
public class ArticleData {

    String mId;
    String mServerId;
    String mTitle;
    String mAuthor;
    String mBody;
    String mThumbUrl;
    String mPhotoUrl;
    String mAspectRatio;
    String mPublishedDate;

    public ArticleData(String mId, String mServerId, String mTitle, String mAuthor, String mBody,
                       String mThumbUrl, String mPhotoUrl, String mAspectRatio, String mPublishedDate) {
        this.mId = mId;
        this.mServerId = mServerId;
        this.mTitle = mTitle;
        this.mAuthor = mAuthor;
        this.mBody = mBody;
        this.mThumbUrl = mThumbUrl;
        this.mPhotoUrl = mPhotoUrl;
        this.mAspectRatio = mAspectRatio;
        this.mPublishedDate = mPublishedDate;
    }

    //constructor for parceler
    public ArticleData() {

    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmBody() {
        return mBody;
    }

    public String getmThumbUrl() {
        return mThumbUrl;
    }

    public String getmPhotoUrl() {
        return mPhotoUrl;
    }

    public String getmPublishedDate() {
        return mPublishedDate;
    }
}
