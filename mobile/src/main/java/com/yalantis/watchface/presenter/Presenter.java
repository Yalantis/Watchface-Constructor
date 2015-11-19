package com.yalantis.watchface.presenter;

import com.yalantis.watchface.view.MvpView;

/**
 * @author andrewkhristyan on 10/16/15.
 */
public interface Presenter<T extends MvpView> {

    void register(T view);

    void unregister(T view);

}
