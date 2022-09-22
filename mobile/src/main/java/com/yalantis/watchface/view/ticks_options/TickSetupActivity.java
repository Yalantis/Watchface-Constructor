package com.yalantis.watchface.view.ticks_options;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;

import com.yalantis.watchface.App;
import com.yalantis.watchface.R;
import com.yalantis.watchface.databinding.ActivityTicksConfigBinding;
import com.yalantis.watchface.presenter.ticks_options.SeekbarPresenterImpl;
import com.yalantis.watchface.view.BaseGoogleApiActivity;


/**
 * @author andrewkhristyan on 10/23/15.
 */
public class TickSetupActivity extends BaseGoogleApiActivity implements SeekbarUpdateMvpView {

    private static final String SECONDS_KEY = "seconds_offset";
    private static final String MINUTES_KEY = "minutes_offset";
    private static final String HOURS_KEY = "hours_offset";


    private SeekbarPresenterImpl mSeekBarPresenter = new SeekbarPresenterImpl();
    private ActivityTicksConfigBinding binding;

    public static Intent newActivity(Context context) {
        Intent intent = new Intent(context, TickSetupActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTicksConfigBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        setTitle(getString(R.string.main_label));

        binding.textViewSecondsOffset.setText(String.valueOf(binding.seekBarSeconds.getProgress()));
        binding.textViewMinuteOffset.setText(String.valueOf(binding.seekBarMinute.getProgress()));
        binding.textViewHoursOffset.setText(String.valueOf(binding.seekBarHour.getProgress()));
        mSeekBarPresenter.register(this);

        binding.seekBarSeconds.setOnSeekBarChangeListener(new SeekBarListener(SECONDS_KEY));
        binding.seekBarHour.setOnSeekBarChangeListener(new SeekBarListener(HOURS_KEY));
        binding.seekBarMinute.setOnSeekBarChangeListener(new SeekBarListener(MINUTES_KEY));
        bindData();
    }

    private void bindData() {
        binding.seekBarSeconds.setProgress(App.getConfigurationManager().getConfigItem(SECONDS_KEY));
        binding.seekBarHour.setProgress(App.getConfigurationManager().getConfigItem(HOURS_KEY));
        binding.seekBarMinute.setProgress(App.getConfigurationManager().getConfigItem(MINUTES_KEY));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSeekBarPresenter.unregister(this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void updateSeekBarLabel(String seekBarName, int offset) {
        if (seekBarName.contains("seconds")) {
            binding.textViewSecondsOffset.setText(String.valueOf(offset));
        } else if (seekBarName.contains("hours")) {
            binding.textViewHoursOffset.setText(String.valueOf(offset));
        } else {
            binding.textViewMinuteOffset.setText(String.valueOf(offset));
        }
    }

    private class SeekBarListener implements SeekBar.OnSeekBarChangeListener {

        private String mSeekBarType;

        public SeekBarListener(String mSeekBarType) {
            this.mSeekBarType = mSeekBarType;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mSeekBarPresenter.updateTicksOffsets(seekBar.getProgress(), mSeekBarType, mGoogleApiClient);
        }
    }
}
