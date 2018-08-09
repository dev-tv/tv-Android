package com.tv.fcmsStagingapp.MainScreen.Deployment.SelectPaymentScreen;

import com.tv.fcmsStagingapp.BaseClasses.BaseInteractor;
import com.tv.fcmsStagingapp.data.network.ApiHelper;
import com.tv.fcmsStagingapp.data.prefs.PreferencesHelper;

import javax.inject.Inject;

/**
 * Created by admin on 16/01/18.
 */

public class SelectPaymentDialogInteractor extends BaseInteractor implements SelectPaymentDialogMvpInteractor {

    @Inject
    public SelectPaymentDialogInteractor(PreferencesHelper preferencesHelper, ApiHelper apiHelper) {
        super(preferencesHelper, apiHelper);
    }
}
