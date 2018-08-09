package com.tv.fcmsStagingapp.MainScreen.Deployment.SelectPaymentScreen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.tv.fcmsStagingapp.BaseClasses.BasePresenter;
import com.tv.fcmsStagingapp.R;
import com.tv.fcmsStagingapp.data.modal.CollectDataRequestModal;
import com.tv.fcmsStagingapp.data.modal.CollectDataResponseModal;
import com.tv.fcmsStagingapp.data.modal.CollectionIdentification;
import com.tv.fcmsStagingapp.data.modal.LocationCollectionData;
import com.tv.fcmsStagingapp.data.network.ApiHelper;
import com.tv.fcmsStagingapp.data.network.retrofit.ApiClient;
import com.tv.fcmsStagingapp.home.MyPreference;
import com.tv.fcmsStagingapp.utils.Logger;
import com.tv.fcmsStagingapp.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 16/01/18.
 */

public class SelectPaymentDialogPresenter<V extends SelectPaymentDialogMvpView, I extends SelectPaymentDialogMvpInteractor> extends
        BasePresenter<V, I> implements SelectPaymentDialogMvpPresenter<V, I> {

    private static final String TAG = SelectPaymentDialogPresenter.class.getSimpleName();

    @Inject
    public SelectPaymentDialogPresenter(I mvpInteractor, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(mvpInteractor, schedulerProvider, compositeDisposable);
    }

    @Override
    public void postCollectedData(String plateNumber,String product,String tinNumber,int  collector_id,
                                  String payment_transaction_id, boolean payment_complete,String payment_method,
                                  int payer_id, int deployment_id,String identification,float multiple_factor,
                                  double lat, double lng) {

        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.pleaseCheckInternetConnectionText);
            return;
        }

        getMvpView().showLoading();
        getMvpView().hideKeyboard();

        ApiHelper mApiHelper = ApiClient.getClient().create(ApiHelper.class);

        CollectDataRequestModal modal = new CollectDataRequestModal();

        List<CollectionIdentification> collectionIdentificationList = new ArrayList<>();
        if (!plateNumber.isEmpty()){
            CollectionIdentification collectionIdentificationPlate = new CollectionIdentification();
            collectionIdentificationPlate.setKey("Plate number");
            collectionIdentificationPlate.setValue(plateNumber);
            collectionIdentificationList.add(collectionIdentificationPlate);

        }
        if (!product.isEmpty()){
            CollectionIdentification collectionIdentificationProduct = new CollectionIdentification();
            collectionIdentificationProduct.setKey("Product");
            collectionIdentificationProduct.setValue(product);
            collectionIdentificationList.add(collectionIdentificationProduct);
        }
        if (!tinNumber.isEmpty()){
            CollectionIdentification collectionIdentificationTin = new CollectionIdentification();
            collectionIdentificationTin.setKey("tin");
            collectionIdentificationTin.setValue(tinNumber);
            collectionIdentificationList.add(collectionIdentificationTin);
        }

        LocationCollectionData locationCollectionData  = new LocationCollectionData();

        locationCollectionData.setLatitude(lat);
        locationCollectionData.setLongitude(lng);

        modal.setLocation(locationCollectionData);

        modal.setCollectionIdentifications(collectionIdentificationList);
        modal.setCollector_id(collector_id);
        modal.setPaymentTransactionId(payment_transaction_id);
        modal.setPaymentComplete(payment_complete);
        modal.setPayment_method(payment_method);
        modal.setPayerId(payer_id);
        modal.setDeploymentId(deployment_id);
        modal.setMultiple_factor(multiple_factor);

        Gson gson = new GsonBuilder().create();

        Logger.logsError(TAG, "JSON requestModal CollectedData Final : " + gson.toJson(modal));
        mApiHelper.postCollectData(MyPreference.getUserAuthToken(), modal).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Logger.logsError(TAG, "onResponse Called Collections: " + response.toString() + " \n call : " + call.request() + "\n " +
                        response.body() + "\n " + response.errorBody() + "\n " + response.code());
                getMvpView().hideLoading();

                if (response.code() == 200 || response.code() == 201) {

                    Logger.logsError(TAG, "response  Header auth-token : " + response.headers().get("auth-token"));
                    Gson mGson1 = new Gson();

                     /*Type listType = new TypeToken<List<MapSearchListResultModal>>() {
                    }.getType();
                    List<MapSearchListResultModal> modalList = (List<MapSearchListResultModal>) mGson1.fromJson(response.body().toString(), listType);*/

                    CollectDataResponseModal modal = mGson1.fromJson(response.body().toString().trim(),
                            CollectDataResponseModal.class);

                    if (modal == null) {
                        getMvpView().showMessage(R.string.some_error);
                        return;
                    }

                    // getMvpView().handleCollectionDataResponse(modal);

                    getMvpView().handleCollectionResponse(modal);
                    Logger.logsError(TAG, "response  BODY Collections: " + response.body().toString());

                }   else if (response.code() == 401) {

                    getMvpView().showMessage(R.string.sessionExpireText);

                    getMvpView().openActivityOnTokenExpire();
                } else {
                    getMvpView().showMessage(R.string.some_error);
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Logger.logsError(TAG, "onFailure Called");
                t.printStackTrace();
                getMvpView().hideLoading();
                getMvpView().onError(R.string.some_error);
            }
        });
    }
}