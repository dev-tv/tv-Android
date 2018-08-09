package com.tv.fcmsStagingapp.MainScreen.Deployment.SelectPaymentScreen;

import com.tv.fcmsStagingapp.BaseClasses.MvpPresenter;

/**
 * Created by admin on 16/01/18.
 */

public interface SelectPaymentDialogMvpPresenter<V extends SelectPaymentDialogMvpView,
        I extends SelectPaymentDialogMvpInteractor> extends MvpPresenter<V, I> {

   // void getFeePayerList(int feePayerId);


    void postCollectedData(String plateNumber,String product,String tinNumber,int collector_id,
                           String payment_transaction_id, boolean payment_complete ,String payment_method,
                           int payer_id, int deployment_id,String identification,float multiple_factor,
                           double lat, double lng);



}
