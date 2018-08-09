package com.tv.fcmsStagingapp.MainScreen.Deployment.SelectPaymentScreen;

import com.tv.fcmsStagingapp.BaseClasses.MvpView;
import com.tv.fcmsStagingapp.data.modal.CollectDataResponseModal;

/**
 * Created by admin on 16/01/18.
 */

public interface SelectPaymentDialogMvpView extends MvpView {
    void handleCollectionResponse(CollectDataResponseModal modal);
    //  void openConfirmScreen(CollectDataPayerResponseModal modal, CollectPayerDataRequestModal collectPayerDataRequestModal);

  //  void populateFeeSettingData(FeeSettingResponseModal feeSettingResponseModal);

   // void openConfirmScreen(CollectDataResponseModal modal);




    //  void populateDeploymentFragData(List<DeploymentModal> deploymentModal);
}
