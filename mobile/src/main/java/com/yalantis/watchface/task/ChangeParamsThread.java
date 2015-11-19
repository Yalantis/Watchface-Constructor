package com.yalantis.watchface.task;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

/**
 * @author andrewkhristyan on 10/22/15.
 */
public class ChangeParamsThread extends Thread {

    private String mSecondsOffset;
    private GoogleApiClient mGoogleApiClient;
    private String mType;

    public ChangeParamsThread(String secondsOffset, String type, GoogleApiClient googleApiClient) {
        mSecondsOffset = secondsOffset;
        mGoogleApiClient = googleApiClient;
        mType = type;
    }

    public void run() {
        NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
        for (Node node : nodes.getNodes()) {
            Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), mType, mSecondsOffset.getBytes()).await();
        }
    }
}
