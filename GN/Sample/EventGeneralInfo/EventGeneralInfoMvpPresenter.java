package com.tv.goin.tabs.Profiles.AddEvent.EventGeneralInfo;

import com.tv.goin.BaseClasses.MvpPresenter;


/**
 * Created by user on 5/9/17.
 */

public interface EventGeneralInfoMvpPresenter<V extends EventGeneralInfoMvpView,
        I extends EventGeneralInfoMvpInteractor> extends MvpPresenter<V,I> {
    void clickToLocation();
}
