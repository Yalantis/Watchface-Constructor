package com.yalantis.watchface.sevrice;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.yalantis.watchface.Constants;
import com.yalantis.watchface.R;

import java.util.HashMap;
import java.util.Map;

public class EagleWatchFaceService extends AbstractAmericanAnalogWatchFaceService {
    private Map<String, Bitmap> map = new HashMap<>();

    @Override
    protected Context getContext() {
        return this;
    }

    @Override
    protected Map<String, Bitmap> getBitmaps() {
        Resources resources = getContext().getResources();
        map.put(Constants.BG_BUNDLE_FLAG, BitmapFactory.decodeResource(resources, R.drawable.bg));
        map.put(Constants.BG_AMBIENT_BUNDLE_FLAG, BitmapFactory.decodeResource(resources, R.drawable.bg_ambient));
        map.put(Constants.SEC_TICK_BUNDLE_FLAG, BitmapFactory.decodeResource(resources, R.drawable.tick));
        map.put(Constants.MIN_TICK_BUNDLE_FLAG, BitmapFactory.decodeResource(resources, R.drawable.min));
        map.put(Constants.HR_TICK_BUNDLE_FLAG, BitmapFactory.decodeResource(resources, R.drawable.hrs));
        map.put(Constants.HR_TICK_AMBIENT_BUNDLE_FLAG, BitmapFactory.decodeResource(resources, R.drawable.hrs_ambient));
        map.put(Constants.MIN_TICK_AMBIENT_BUNDLE_FLAG, BitmapFactory.decodeResource(resources, R.drawable.min_ambient));
        return map;
    }
}
