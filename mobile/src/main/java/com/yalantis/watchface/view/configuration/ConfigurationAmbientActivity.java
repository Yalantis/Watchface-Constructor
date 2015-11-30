package com.yalantis.watchface.view.configuration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.yalantis.watchface.Constants;
import com.yalantis.watchface.R;
import com.yalantis.watchface.presenter.configuration.ConfigurationPresenter;
import com.yalantis.watchface.presenter.configuration.ConfigurationPresenterImpl;
import com.yalantis.watchface.view.BaseGoogleApiActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author andrewkhristyan on 10/27/15.
 */
public class ConfigurationAmbientActivity extends BaseGoogleApiActivity implements ConfigurationMvpView{

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private ConfigurationPresenter mConfigurationPresenter = new ConfigurationPresenterImpl();

    public static Intent newActivity(Context context) {
        Intent intent = new Intent(context, ConfigurationAmbientActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambient);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.main_label));
        mConfigurationPresenter.register(this);
    }

    @OnClick(R.id.button_change_background)
    void onClickChangeBackgroundAmbient() {
        mConfigurationPresenter.changeContentImage(isConnected, Constants.BACKGROUND_AMBIENT);
    }

    @OnClick(R.id.button_change_ambient_hour)
    void onClickChangeHourTick() {
        mConfigurationPresenter.changeContentImage(isConnected, Constants.HOUR_AMBIENT);
    }

    @OnClick(R.id.button_change_ambient_minute)
    void onClickChangeMinuteTick() {
        mConfigurationPresenter.changeContentImage(isConnected, Constants.MINUTE_AMBIENT);
    }

    @Override
    public void startFileChooser(Intent intent, int requestCode) {
        startActivityForResult(
                Intent.createChooser(intent, getString(R.string.select_file)),
                requestCode);
    }

    @Override
    public void showStatusMessage(String message) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mConfigurationPresenter.onActivityResult(resultCode, data, requestCode, mGoogleApiClient);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mConfigurationPresenter.unregister(this);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
