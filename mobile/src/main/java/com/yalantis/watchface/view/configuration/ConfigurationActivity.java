package com.yalantis.watchface.view.configuration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.yalantis.watchface.Constants;
import com.yalantis.watchface.R;
import com.yalantis.watchface.databinding.ActivityConfigurationBinding;
import com.yalantis.watchface.presenter.configuration.ConfigurationPresenter;
import com.yalantis.watchface.presenter.configuration.ConfigurationPresenterImpl;
import com.yalantis.watchface.view.BaseGoogleApiActivity;
import com.yalantis.watchface.view.ticks_options.TickSetupActivity;

/**
 * @author andrewkhristyan on 10/5/15.
 */
public class ConfigurationActivity extends BaseGoogleApiActivity implements ConfigurationMvpView {

    protected ConfigurationPresenter mConfigurationPresenter = new ConfigurationPresenterImpl();

    private ActivityConfigurationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfigurationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        setTitle(getString(R.string.main_label));
        mConfigurationPresenter.register(this);

        setupListeners();
    }

    private void setupListeners() {
        binding.buttonChangeBackground.setOnClickListener(v ->
                mConfigurationPresenter.changeContentImage(isConnected, Constants.BACKGROUND_CHOOSER)
        );

        binding.buttonChangeSecondTick.setOnClickListener(v ->
                mConfigurationPresenter.changeContentImage(isConnected, Constants.SECOND_CHOOSER)
        );

        binding.buttonChangeHoursTicks.setOnClickListener(v ->
                mConfigurationPresenter.changeContentImage(isConnected, Constants.HOUR_CHOOSER)
        );

        binding.buttonChangeMinuteTicks.setOnClickListener(v ->
                mConfigurationPresenter.changeContentImage(isConnected, Constants.MINUTE_CHOOSER)
        );

        binding.buttonChangeSaveConfiguration.setOnClickListener(v -> {
            mConfigurationPresenter.saveConfig();
            Snackbar.make(binding.linearLayoutRoot, getString(R.string.saved_message), Snackbar.LENGTH_SHORT)
                    .show();
        });

        binding.buttonTicksConfiguration.setOnClickListener(v ->
                startActivity(TickSetupActivity.newActivity(this))
        );

        binding.buttonAmbient.setOnClickListener(v ->
                startActivity(ConfigurationAmbientActivity.newActivity(this))
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mConfigurationPresenter.unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mConfigurationPresenter.onActivityResult(resultCode, data, requestCode, mGoogleApiClient);
    }

    @Override
    public void startFileChooser(Intent intent, int requestCode) {
        startActivityForResult(
                Intent.createChooser(intent, getString(R.string.select_file)),
                requestCode);
    }

    @Override
    public void showStatusMessage(String message) {
        Snackbar.make(findViewById(R.id.linear_layout_root), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
