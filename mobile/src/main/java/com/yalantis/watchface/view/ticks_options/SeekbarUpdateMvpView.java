package com.yalantis.watchface.view.ticks_options;

import com.yalantis.watchface.view.MvpView;

/**
 * @author andrewkhristyan on 10/23/15.
 */
public interface SeekbarUpdateMvpView extends MvpView{

    void updateSeekBarLabel(String seekBarName, int secondOffset);

}
