package com.yalantis.watchface.sevrice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.wearable.watchface.WatchFaceStyle;

import com.google.android.gms.wearable.MessageEvent;
import com.yalantis.watchface.events.WatchfaceUpdatedEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by andrewkhristyan on 10/6/15.
 */
public class WearableListenerService extends com.google.android.gms.wearable.WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().contains("offset")) {
            EventBus.getDefault().post(new WatchfaceUpdatedEvent(messageEvent.getPath(), Integer.parseInt(new String(messageEvent.getData()))));
        } else {
            byte[] message = messageEvent.getData();
            Bitmap bitmap = BitmapFactory.decodeByteArray(message, 0, message.length, null);
            EventBus.getDefault().post(new WatchfaceUpdatedEvent(messageEvent.getPath(), bitmap));
        }
    }
}
