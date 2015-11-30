package com.yalantis.watchface.view.ticks_options;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yalantis.watchface.App;
import com.yalantis.watchface.R;
import com.yalantis.watchface.presenter.ticks_options.SeekbarPresenterImpl;
import com.yalantis.watchface.view.BaseGoogleApiActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author andrewkhristyan on 10/23/15.
 */
public class TickSetupActivity extends BaseGoogleApiActivity implements SeekbarUpdateMvpView {

    private static final String SECONDS_KEY = "seconds_offset";
    private static final String MINUTES_KEY = "minutes_offset";
    private static final String HOURS_KEY = "hours_offset";


    private SeekbarPresenterImpl mSeekBarPresenter = new SeekbarPresenterImpl();

    @Bind(R.id.seek_bar_seconds)
    SeekBar mSeekBarSeconds;
    @Bind(R.id.text_view_seconds_offset)
    TextView mTextViewSecondsOffset;
    @Bind(R.id.seek_bar_minute)
    SeekBar mSeekBarMinutes;
    @Bind(R.id.text_view_minute_offset)
    TextView mTextViewMinutesOffset;
    @Bind(R.id.seek_bar_hour)
    SeekBar mSeekBarHours;
    @Bind(R.id.text_view_hours_offset)
    TextView mTextViewHoursOffset;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    public static Intent newActivity(Context context) {
        Intent intent = new Intent(context, TickSetupActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticks_config);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setTitle(getString(R.string.main_label));

        mTextViewSecondsOffset.setText(String.valueOf(mSeekBarSeconds.getProgress()));
        mTextViewMinutesOffset.setText(String.valueOf(mSeekBarMinutes.getProgress()));
        mTextViewHoursOffset.setText(String.valueOf(mSeekBarHours.getProgress()));
        mSeekBarPresenter.register(this);

        mSeekBarSeconds.setOnSeekBarChangeListener(new SeekBarListener(SECONDS_KEY));
        mSeekBarHours.setOnSeekBarChangeListener(new SeekBarListener(HOURS_KEY));
        mSeekBarMinutes.setOnSeekBarChangeListener(new SeekBarListener(MINUTES_KEY));
        bindData();
    }

    private void bindData() {
        mSeekBarSeconds.setProgress(App.getConfigurationManager().getConfigItem(SECONDS_KEY));
        mSeekBarHours.setProgress(App.getConfigurationManager().getConfigItem(HOURS_KEY));
        mSeekBarMinutes.setProgress(App.getConfigurationManager().getConfigItem(MINUTES_KEY));
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
        if(seekBarName.contains("seconds")) {
            mTextViewSecondsOffset.setText(String.valueOf(offset));
        } else if(seekBarName.contains("hours")) {
            mTextViewHoursOffset.setText(String.valueOf(offset));
        } else {
            mTextViewMinutesOffset.setText(String.valueOf(offset));
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
