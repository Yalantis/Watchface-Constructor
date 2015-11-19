package com.yalantis.watchface.presenter.ticks_options;

import com.google.android.gms.common.api.GoogleApiClient;
import com.yalantis.watchface.App;
import com.yalantis.watchface.task.ChangeParamsThread;
import com.yalantis.watchface.view.ticks_options.SeekbarUpdateMvpView;

/**
 * @author andrewkhristyan on 10/23/15.
 */
public class SeekbarPresenterImpl implements SeekbarPresenter {

    private SeekbarUpdateMvpView mSeekbarUpdateView;

    @Override
    public void register(SeekbarUpdateMvpView view) {
        mSeekbarUpdateView = view;
    }

    @Override
    public void unregister(SeekbarUpdateMvpView view) {
        mSeekbarUpdateView = null;
    }

    @Override
    public void updateTicksOffsets(int progress, String tickKey, GoogleApiClient googleApiClient) {
        new ChangeParamsThread(String.valueOf(progress), tickKey, googleApiClient).start();
        App.getConfigurationManager().updateConfigurationParameter(tickKey, progress);
        mSeekbarUpdateView.updateSeekBarLabel(tickKey, progress);
    }
}
