package com.yalantis.watchface.view.ticks_options;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

    public static Intent newActivity(Context context) {
        Intent intent = new Intent(context, TickSetupActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticks_config);
        ButterKnife.bind(this);

        mTextViewSecondsOffset.setText(String.valueOf(mSeekBarSeconds.getProgress()));
        mSeekBarPresenter.register(this);

        mSeekBarSeconds.setOnSeekBarChangeListener(new SeekBarListener("seconds_offset"));
        mSeekBarHours.setOnSeekBarChangeListener(new SeekBarListener("hours_offset"));
        mSeekBarMinutes.setOnSeekBarChangeListener(new SeekBarListener("minutes_offset"));
        bindData();
    }

    private void bindData() {
        mSeekBarSeconds.setProgress(App.getConfigurationManager().getConfigItem("seconds_offset"));
        mSeekBarHours.setProgress(App.getConfigurationManager().getConfigItem("hours_offset"));
        mSeekBarMinutes.setProgress(App.getConfigurationManager().getConfigItem("minutes_offset"));
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
