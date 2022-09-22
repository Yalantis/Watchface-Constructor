package com.yalantis.watchface.view.configuration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yalantis.watchface.Constants;
import com.yalantis.watchface.R;
import com.yalantis.watchface.databinding.ActivityAmbientBinding;
import com.yalantis.watchface.presenter.configuration.ConfigurationPresenter;
import com.yalantis.watchface.presenter.configuration.ConfigurationPresenterImpl;
import com.yalantis.watchface.view.BaseGoogleApiActivity;


/**
 * @author andrewkhristyan on 10/27/15.
 */
public class ConfigurationAmbientActivity extends BaseGoogleApiActivity implements ConfigurationMvpView {

    private ActivityAmbientBinding binding;
    private ConfigurationPresenter mConfigurationPresenter = new ConfigurationPresenterImpl();

    public static Intent newActivity(Context context) {
        Intent intent = new Intent(context, ConfigurationAmbientActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAmbientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        setTitle(getString(R.string.main_label));
        mConfigurationPresenter.register(this);

        setupListners();
    }

    private void setupListners() {
        binding.buttonChangeBackground.setOnClickListener(v ->
                mConfigurationPresenter.changeContentImage(isConnected, Constants.BACKGROUND_AMBIENT)
        );
        binding.buttonChangeAmbientHour.setOnClickListener(v ->
                mConfigurationPresenter.changeContentImage(isConnected, Constants.HOUR_AMBIENT)
        );
        binding.buttonChangeAmbientMinute.setOnClickListener(v ->
                mConfigurationPresenter.changeContentImage(isConnected, Constants.MINUTE_AMBIENT)
        );
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
