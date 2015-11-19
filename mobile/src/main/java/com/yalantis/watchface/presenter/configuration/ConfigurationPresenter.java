package com.yalantis.watchface.presenter.configuration;

import android.content.Intent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.yalantis.watchface.presenter.Presenter;
import com.yalantis.watchface.view.configuration.ConfigurationMvpView;

/**
 * @author andrewkhristyan on 10/26/15.
 */
public interface ConfigurationPresenter extends Presenter<ConfigurationMvpView> {

    void onActivityResult(int resultCode, Intent data, int requestCode, GoogleApiClient googleApiClient);

    void changeContentImage(boolean isConnected, int type);

    void saveConfig();

}
