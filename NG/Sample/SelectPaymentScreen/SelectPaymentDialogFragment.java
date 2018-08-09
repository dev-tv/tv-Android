package com.tv.fcmsStagingapp.MainScreen.Deployment.SelectPaymentScreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tv.fcmsStagingapp.BaseClasses.BaseDialogFullScreen;
import com.tv.fcmsStagingapp.BaseClasses.DialogInterface;
import com.tv.fcmsStagingapp.CallbackInterface.CallbackSelectPaymentMethod;
import com.tv.fcmsStagingapp.MainScreen.Deployment.PaymentMethodScreen.PaymentMethodDialogFragment;
import com.tv.fcmsStagingapp.MainScreen.Deployment.SuccessScreen.SuccessDialogFragment;
import com.tv.fcmsStagingapp.R;
import com.tv.fcmsStagingapp.adapter.SelectPaymentAdapter;
import com.tv.fcmsStagingapp.data.modal.CollectDataPayerResponseModal;
import com.tv.fcmsStagingapp.data.modal.CollectDataResponseModal;
import com.tv.fcmsStagingapp.data.modal.CollectionModal;
import com.tv.fcmsStagingapp.data.modal.ConfirmationModal;
import com.tv.fcmsStagingapp.data.modal.DeploymentModal;
import com.tv.fcmsStagingapp.data.modal.SelectPaymentModal;
import com.tv.fcmsStagingapp.di.component.ActivityComponent;
import com.tv.fcmsStagingapp.home.MyPreference;
import com.tv.fcmsStagingapp.utils.CommonUtils;
import com.tv.fcmsStagingapp.utils.Constant;
import com.tv.fcmsStagingapp.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dev-20 on 5/22/18.
 */

public class SelectPaymentDialogFragment extends BaseDialogFullScreen implements CallbackSelectPaymentMethod, SelectPaymentDialogMvpView {

    @Inject
    SelectPaymentDialogMvpPresenter<SelectPaymentDialogMvpView, SelectPaymentDialogMvpInteractor> mPresenter;

    @Inject
    SelectPaymentAdapter selectPaymentAdapter;

    private List<SelectPaymentModal> selectPaymentModalList;

    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    @BindView(R.id.left_icon_1_iv)
    ImageView left_icon_1_iv;

    @BindView(R.id.selectPaymentRv)
    RecyclerView selectPaymentRv;

    @BindView(R.id.selectPaymentTv)
    TextView selectPaymentTv;


    @OnClick(R.id.left_icon_1_iv)
    void onclickleft_icon_1_iv(View view) {
        dismiss();
    }

    private static final String TAG = SelectPaymentDialogFragment.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_payment_fragment, container, false);
        ButterKnife.bind(this, view);
        ActivityComponent component = getActivityComponent();
//        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialogFullscreen);

        if (component != null) {
            component.inject(this);
            mPresenter.onAttach(this);
        }
        return view;
    }


    @Override
    public void showDialog(String title, String msg1, String msg2, int type, DialogInterface mDialogInterface) {

    }

    public static SelectPaymentDialogFragment newInstance(Bundle bundle) {
        SelectPaymentDialogFragment fragment = new SelectPaymentDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }

    @Override
    protected void setUp(View view) {


        initData();
        setFonts();
    }

    private void setFonts() {
        toolbar_title.setTypeface(CommonUtils.setBoldFont(getActivity()));
        selectPaymentTv.setTypeface(CommonUtils.setBoldFont(getActivity()));
    }

    private List<CollectionModal> finishCollectionModalList;
    private CollectDataPayerResponseModal collectDataResponseModal;
    private DeploymentModal deploymentModal;
    private ConfirmationModal confirmationModal;
    private ConfirmationModal marketConfirmationModal;
    private SelectPaymentModal selectPaymentModal;

    private float multiple_factor;


    private void initData() {

        if (getArguments() != null) {
            if (getArguments().containsKey(Constant.REMIT_COLLECTION_LIST)) {
                finishCollectionModalList = (List<CollectionModal>) getArguments().getSerializable(Constant.REMIT_COLLECTION_LIST);
                Logger.logsError(TAG, "finishCollectionModalList====== " + finishCollectionModalList.size());
            }

            if (getArguments().containsKey(Constant.COLLECT_PAYER_DATUM)) {
                collectDataResponseModal = (CollectDataPayerResponseModal) getArguments().getSerializable(Constant.COLLECT_PAYER_DATUM);

            }
            if (getArguments().containsKey(Constant.DEPLOYMENT_DATUM)) {
                deploymentModal = (DeploymentModal) getArguments().getSerializable(Constant.DEPLOYMENT_DATUM);

            }
            if (getArguments().containsKey(Constant.CONFIRMATION_DATUM)) {
                confirmationModal = (ConfirmationModal) getArguments().getSerializable(Constant.CONFIRMATION_DATUM);

            }if (getArguments().containsKey(Constant.MARKET_CONFIRMATION_MODAL)) {
                marketConfirmationModal = (ConfirmationModal) getArguments().getSerializable(Constant.MARKET_CONFIRMATION_MODAL);

            }
            if (getArguments().containsKey(Constant.MULTIPLE_FACTOR)) {
                multiple_factor = (float) getArguments().getSerializable(Constant.MULTIPLE_FACTOR);

            }
        }

        selectPaymentModalList = new ArrayList<>();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        selectPaymentRv.setLayoutManager(layoutManager);
        selectPaymentRv.setItemAnimator(new DefaultItemAnimator());
        selectPaymentRv.setAdapter(selectPaymentAdapter);
        selectPaymentRv.setNestedScrollingEnabled(false);
        selectPaymentAdapter.setCallback(this);
/*
        selectPaymentRv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), MainNavigateActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                startActivitySideWiseAnimation();

                //   VehicleDetailDialogFragment.newInstance(null).show(getFragmentManager());
            }
        }));*/

        dummyData();
        toolbar_title.setText(getResources().getString(R.string.paymentText));
        toolbar_title.setTextColor(getResources().getColor(R.color.white));
        left_icon_1_iv.setImageResource(R.drawable.white_back);
    }

    private void dummyData() {

        selectPaymentModalList.add(new SelectPaymentModal("CASH"));
        selectPaymentModalList.add(new SelectPaymentModal("MTN"));
        selectPaymentModalList.add(new SelectPaymentModal("TIGO"));
        selectPaymentModalList.add(new SelectPaymentModal("AIRTEL"));
        selectPaymentModalList.add(new SelectPaymentModal("BANK"));

        selectPaymentAdapter.addItems(selectPaymentModalList);
    }

    @Override
    public void selectCashPayment(SelectPaymentModal selectPaymentModal, int position) {

        this.selectPaymentModal = selectPaymentModal;

     /*   mPresenter.postCollectedData(deploymentModal.getCollection_identification_type(),
                deploymentModal.getCollector(), "", true,
                Constant.Cash, String.valueOf((collectDataResponseModal.getId())), deploymentModal.getId(),
                collectDataResponseModal.getIdentification(),multiple_factor);*/

        if (confirmationModal != null) {

            mPresenter.postCollectedData(confirmationModal.getPlateNumber(), confirmationModal.getProduct(), confirmationModal.getTinNumber(),
                    Integer.parseInt(MyPreference.getCollectorId()), "", true,
                    Constant.Cash, (collectDataResponseModal.getId()), deploymentModal.getId(),
                    collectDataResponseModal.getIdentification(), multiple_factor, MyPreference.getDouble(Constant.LAT),
                    MyPreference.getDouble(Constant.LNG));
        } else {

            mPresenter.postCollectedData("", "", marketConfirmationModal.getTinNumber(),
                    Integer.parseInt(MyPreference.getCollectorId()), "", true,
                    Constant.Cash, (collectDataResponseModal.getId()), deploymentModal.getId(),
                    collectDataResponseModal.getIdentification(), multiple_factor, MyPreference.getDouble(Constant.LAT),
                    MyPreference.getDouble(Constant.LNG));
        }

    }

    @Override
    public void selectMobilePayment(SelectPaymentModal selectPaymentModal, int position) {

        this.selectPaymentModal = selectPaymentModal;

        if (this.selectPaymentModal != null) {

            String paymentMethod = "";
            if (this.selectPaymentModal.getName().equalsIgnoreCase(Constant.MTN)) {
                paymentMethod = Constant.MTN;

            }
            if (this.selectPaymentModal.getName().equalsIgnoreCase(Constant.TIGO)) {

                paymentMethod = Constant.TIGO;
            }

            if (this.selectPaymentModal.getName().equalsIgnoreCase(Constant.AIRTEL)) {

                paymentMethod = Constant.AIRTEL;
            }

            if (confirmationModal != null) {
                mPresenter.postCollectedData(confirmationModal.getPlateNumber(), confirmationModal.getProduct(),confirmationModal.getTinNumber(),
                        Integer.parseInt(MyPreference.getCollectorId()), "", true,
                        paymentMethod, (collectDataResponseModal.getId()), deploymentModal.getId(),
                        collectDataResponseModal.getIdentification(), multiple_factor, MyPreference.getDouble(Constant.LAT),
                        MyPreference.getDouble(Constant.LNG));
            } else {
                mPresenter.postCollectedData("", "",marketConfirmationModal.getTinNumber(),
                        Integer.parseInt(MyPreference.getCollectorId()), "", true,
                        paymentMethod, (collectDataResponseModal.getId()), deploymentModal.getId(),
                        collectDataResponseModal.getIdentification(), multiple_factor, MyPreference.getDouble(Constant.LAT),
                        MyPreference.getDouble(Constant.LNG));
            }

        }
    }

    @Override
    public void selectBankPayment(SelectPaymentModal paymentMethodModal, int layoutPosition) {

        if (paymentMethodModal != null) {
            if (paymentMethodModal.getName().equalsIgnoreCase(Constant.BANK)) {

                // bundle.putSerializable(Constant.BANK, true);
                /*Intent intent = new Intent(getActivity(), MainNavigateActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                startActivitySideWiseAnimation();*/

            }
        }
    }

    @Override
    public void handleCollectionResponse(CollectDataResponseModal modal) {

        if (modal != null) {

            if (this.selectPaymentModal != null) {
                if (this.selectPaymentModal.getName().equalsIgnoreCase(Constant.CASH)) {
                    SuccessDialogFragment.newInstance(null).show(getFragmentManager());
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.COLLECT_DATA, modal);
                    if (this.selectPaymentModal.getName().equalsIgnoreCase(Constant.TIGO)) {
                        bundle.putSerializable(Constant.TIGO, true);

                    }
                    if (this.selectPaymentModal.getName().equalsIgnoreCase(Constant.MTN)) {
                        bundle.putSerializable(Constant.MTN, true);
                    }
                    if (this.selectPaymentModal.getName().equalsIgnoreCase(Constant.AIRTEL)) {
                        bundle.putSerializable(Constant.AIRTEL, true);
                    }

                    PaymentMethodDialogFragment.newInstance(bundle).show(getFragmentManager());
                }

            }
        }
    }

}
