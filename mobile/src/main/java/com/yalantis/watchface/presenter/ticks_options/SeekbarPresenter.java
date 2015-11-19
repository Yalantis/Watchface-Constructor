package com.yalantis.watchface.presenter.ticks_options;

import com.google.android.gms.common.api.GoogleApiClient;
import com.yalantis.watchface.presenter.Presenter;
import com.yalantis.watchface.view.ticks_options.SeekbarUpdateMvpView;

/**
 * @author andrewkhristyan on 10/27/15.
 */
public interface SeekbarPresenter extends Presenter<SeekbarUpdateMvpView>{

    void updateTicksOffsets(int progress, String tickKey, GoogleApiClient googleApiClient);

}
