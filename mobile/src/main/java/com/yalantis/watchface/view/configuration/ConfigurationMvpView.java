package com.yalantis.watchface.view.configuration;

import android.content.Intent;

import com.yalantis.watchface.view.MvpView;

/**
 * @author andrewkhristyan on 10/16/15.
 */
public interface ConfigurationMvpView extends MvpView {

    void startFileChooser(Intent intent, int requestCode);

    void showStatusMessage(String message);

}
