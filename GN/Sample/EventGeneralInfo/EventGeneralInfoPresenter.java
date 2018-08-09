package com.tv.goin.tabs.Profiles.AddEvent.EventGeneralInfo;

import com.tv.goin.BaseClasses.BasePresenter;
import com.tv.goin.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by user on 5/9/17.
 */

public class EventGeneralInfoPresenter <V extends EventGeneralInfoMvpView, I extends EventGeneralInfoMvpInteractor>
        extends BasePresenter<V, I> implements EventGeneralInfoMvpPresenter<V, I> {

    @Inject
    public EventGeneralInfoPresenter(I mvpInteractor, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(mvpInteractor, schedulerProvider, compositeDisposable);
    }

    @Override
    public void clickToLocation() {
        getMvpView().clickToLocation();
    }
}