package com.yalantis.watchface.events;

import android.graphics.Bitmap;

/**
 * Created by andrewkhristyan on 10/7/15.
 */
public class WatchfaceUpdatedEvent {

    private String mResourceKey;
    private Bitmap mResourceBitmap;
    private int mOffset;

    public WatchfaceUpdatedEvent(String resourceKey, Bitmap resourceBitmap) {
        mResourceKey = resourceKey;
        mResourceBitmap = resourceBitmap;
    }

    public WatchfaceUpdatedEvent(String key, int offset) {
        mOffset = offset;
        mResourceKey = key;
    }

    public String getResourceKey() {
        return mResourceKey;
    }

    public Bitmap getResourceBitmap() {
        return mResourceBitmap;
    }

    public int getOffset() {
        return mOffset;
    }
}
