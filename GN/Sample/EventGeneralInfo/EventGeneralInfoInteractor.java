package com.tv.goin.tabs.Profiles.AddEvent.EventGeneralInfo;

import com.tv.goin.BaseClasses.BaseInteractor;
import com.tv.goin.data.network.ApiHelper;
import com.tv.goin.data.prefs.PreferencesHelper;

import javax.inject.Inject;

/**
 * Created by user on 5/9/17.
 */

public class EventGeneralInfoInteractor extends BaseInteractor implements EventGeneralInfoMvpInteractor {

    @Inject
    public EventGeneralInfoInteractor(PreferencesHelper preferencesHelper, ApiHelper apiHelper) {
        super(preferencesHelper, apiHelper);
    }
}