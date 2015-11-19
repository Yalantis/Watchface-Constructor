package com.yalantis.watchface.presenter.configuration;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.yalantis.watchface.App;
import com.yalantis.watchface.Constants;
import com.yalantis.watchface.task.SendToDataLayerThread;
import com.yalantis.watchface.view.configuration.ConfigurationMvpView;

import java.io.IOException;

/**
 * @author andrewkhristyan on 10/16/15.
 */
public class ConfigurationPresenterImpl implements ConfigurationPresenter, SendToDataLayerThread.DataLayerListener {

    private ConfigurationMvpView mConfigurationView;

    @Override
    public void register(ConfigurationMvpView holder) {
        mConfigurationView = holder;
    }

    @Override
    public void unregister(ConfigurationMvpView holder) {
        mConfigurationView = null;
    }


    @Override
    public void onActivityResult(int resultCode, Intent data, int requestCode, GoogleApiClient googleApiClient) {
        try {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImageUri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mConfigurationView.getContext().getContentResolver(), selectedImageUri);
                App.getConfigurationManager().updateField(requestCode, bitmap);
                new SendToDataLayerThread(Constants.resourceKeyMap.get(requestCode), bitmap, googleApiClient, this).start();
            }
        } catch (IOException e) {
            Log.e("TAG", e.getMessage());
        }
    }

    @Override
    public void changeContentImage(boolean isConnected, int type) {
        if(isConnected) {
            startFileChooser(type);
        }
    }

    private void startFileChooser(int fileSelectCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        mConfigurationView.startFileChooser(intent, fileSelectCode);
    }

    @Override
    public void saveConfig() {
        App.getConfigurationManager().saveConfiguration();
    }

    @Override
    public void onSuccess(String message) {
        mConfigurationView.showStatusMessage(message);
    }
}
