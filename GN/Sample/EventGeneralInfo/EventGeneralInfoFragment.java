package com.tv.goin.tabs.Profiles.AddEvent.EventGeneralInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tv.goin.BaseClasses.BaseFragment;
import com.tv.goin.R;
import com.tv.goin.data.modals.Events.EventDatum;
import com.tv.goin.di.component.ActivityComponent;
import com.tv.goin.tabs.Profiles.AddEvent.AddEventProfileActivity;
import com.tv.goin.utils.CommonUtils;
import com.tv.goin.utils.Constant;
import com.tv.goin.utils.Logger;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * Created by user on 5/9/17.
 */

public class EventGeneralInfoFragment extends BaseFragment implements EventGeneralInfoMvpView {


    @BindView(R.id.next_btn)
    Button next_btn;
    @BindView(R.id.genral_info_tv)
    TextView genral_info_tv;
    @BindView(R.id.event_name_et)
    EditText event_name_et;
    @BindView(R.id.descrip_et)
    EditText descrip_et;
    @BindView(R.id.datetime_tv)
    TextView datetime_tv;
    @BindView(R.id.startingdatetime_et)
    EditText startingdatetime_et;
    @BindView(R.id.enddatetime_et)
    EditText enddatetime_et;
    @BindView(R.id.bottomtext_tv)
    TextView bottomtext_tv;

    @OnClick(R.id.startingdatetime_et)
    void onClickstartingdatetime_et(View v) {
        showDatePickerForStartTime();
    }


    @OnClick(R.id.enddatetime_et)
    void onClickenddatetime_et(View v) {
        showDatePickerForEndingTime();
    }


    @OnClick(R.id.next_btn)
    void clickToLocation(View v) {
        mPresenter.clickToLocation();
    }

    private EventDatum mEventDatum;
    private Long endTime;
    private Long startTime;

    @Override
    protected void setUp(View view) {
        if (getArguments() != null) {
            if (getArguments().containsKey(Constant.EVENT_DATUM)) {
                mEventDatum = (EventDatum) getArguments().getSerializable(Constant.EVENT_DATUM);
                if (mEventDatum!=null&&mEventDatum.getSequence()!=0){
                    populateEventGeneralInfo(mEventDatum);
                }
            }
        }
        setTypeface();
    }

    private void populateEventGeneralInfo(EventDatum mEventDatum) {
        if (mEventDatum==null){
            return;
        }




        try {
            String mEventDescription = URLDecoder.decode(mEventDatum.getName(), "UTF-8");
            event_name_et.setText(mEventDescription);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            event_name_et.setText(mEventDatum.getName());
        }catch (Exception e){
            e.printStackTrace();
            event_name_et.setText(mEventDatum.getName());
        }

        descrip_et.setText(mEventDatum.getDescription());
        if (mEventDatum.getStartingDate()!=0){
            startTime = mEventDatum.getStartingDate();
            Calendar mCalendar = Calendar.getInstance();
            mCalendar.setTimeInMillis(mEventDatum.getStartingDate());
            String startTimeStr = mCalendar.get(Calendar.YEAR) + "-" +
                    String.format("%02d", (mCalendar.get(Calendar.MONTH))) + "-" +
                    String.format("%02d", mCalendar.get(Calendar.DAY_OF_MONTH) + 1) + " " +
                    String.format("%02d", mCalendar.get(Calendar.HOUR_OF_DAY)) + ":" +
                    String.format("%02d", mCalendar.get(Calendar.MINUTE));
            startingdatetime_et.setText(startTimeStr);
        }
        if (mEventDatum.getEndingDate()!=0){
            endTime = mEventDatum.getEndingDate();
            Calendar mCalendar = Calendar.getInstance();
            mCalendar.setTimeInMillis(mEventDatum.getEndingDate());

            String endDateTime = mCalendar.get(Calendar.YEAR) + "-" +
                    String.format("%02d", (mCalendar.get(Calendar.MONTH))) + "-" +
                    String.format("%02d", mCalendar.get(Calendar.DAY_OF_MONTH) + 1) + " " +
                    String.format("%02d", mCalendar.get(Calendar.HOUR_OF_DAY)) + ":" +
                    String.format("%02d", mCalendar.get(Calendar.MINUTE));
            enddatetime_et.setText(endDateTime);

        }



    }


    @Inject
    EventGeneralInfoMvpPresenter<EventGeneralInfoMvpView, EventGeneralInfoMvpInteractor> mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.general_info_profile_fragment, container, false);
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
        }
        return view;
    }


    private void showDatePickerForStartTime() {
        final Calendar now = Calendar.getInstance();
        if (startTime!=null&&startTime>0){
            now.setTimeInMillis(startTime);
        }


        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, final int year, final int monthOfYear, final int dayOfMonth) {
                        TimePickerDialog mTimePickerDialog =
                                new TimePickerDialog().
                                        newInstance(
                                                new TimePickerDialog.OnTimeSetListener() {
                                                    @Override
                                                                                                    public void onTimeSet(TimePickerDialog view,
                                                                                                                          int hourOfDay, int minute, int second) {
                                                                                                        Calendar calendar = Calendar.getInstance();
                                                                                                        calendar.set(year, (monthOfYear), dayOfMonth,
                                                                                                                hourOfDay, minute, second);
                                                                                                        startTime = calendar.getTimeInMillis();
                                                                                                        Logger.logsError(TAG, "Start Time ms : " + startTime);

                                                                                                        String startTimeStr = year + "-" +
                                                                                                                String.format("%02d", (monthOfYear + 1)) + "-" +
                                                                                                                String.format("%02d", dayOfMonth) + " " +
                                                                                                                String.format("%02d", hourOfDay) + ":" +
                                                                                                                String.format("%02d", minute);/* + ":" +
                                                                                                                String.format("%02d", second);*/
                                                                                                        startingdatetime_et.setText(startTimeStr);


                                                                                                    }
                                                                                                }, now.get(Calendar.HOUR_OF_DAY)
                                , now.get(Calendar.MINUTE)
                                , now.get(Calendar.SECOND), false);

                        mTimePickerDialog.setTitle(getResources().getString(R.string.enddatetime));
                        mTimePickerDialog.setAccentColor(ContextCompat.getColor(getActivity(), R.color.yellow));


                        Calendar mSelecteDateCal = Calendar.getInstance();
                        mSelecteDateCal.set(year, (monthOfYear), dayOfMonth);

                        Date mCurrentDate = new Date(System.currentTimeMillis());

                        Date mSelectedData  = new Date(mSelecteDateCal.getTimeInMillis());

                        int result = mCurrentDate.compareTo(mSelectedData);
                        /**result will be an int < 0 if now Date is less than the theOtherDate Date, 0 if they are equal, and an int > 0 if this Date is greater.*/
                        

                        if (result>=0){
                            Timepoint mTimepoint = new Timepoint(now.get(Calendar.HOUR_OF_DAY)
                                    , now.get(Calendar.MINUTE)
                                    , now.get(Calendar.SECOND));
                            mTimePickerDialog.setMinTime(mTimepoint);
                        }

                        mTimePickerDialog.show(getActivity().getFragmentManager(), "Timepickerdialog");

                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setTitle(getResources().getString(R.string.startdatetime));
        dpd.setAccentColor(ContextCompat.getColor(getActivity(), R.color.yellow));
        final Calendar mCurrentTIme= Calendar.getInstance();

        dpd.setMinDate(mCurrentTIme);
/*
        Calendar minDateCal = Calendar.getInstance();

        dpd.setMaxDate(minDateCal);*/


        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }


    private void showDatePickerForEndingTime() {
        final Calendar now = Calendar.getInstance();

//for default time
        if (endTime!=null&&endTime>0){
            now.setTimeInMillis(endTime);
        }

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog mDatePickerDialogView, final int year, final int monthOfYear, final int dayOfMonth) {
                        TimePickerDialog mTimePickerDialog = new TimePickerDialog().newInstance(new TimePickerDialog.OnTimeSetListener() {
                                                                                                    @Override
                                                                                                    public void onTimeSet(TimePickerDialog view,
                                                                                                                          int hourOfDay, int minute, int second) {
                                                                                                        Calendar calendar = Calendar.getInstance();
                                                                                                        calendar.set(year, (monthOfYear), dayOfMonth,
                                                                                                                hourOfDay, minute, second);
                                                                                                        endTime = calendar.getTimeInMillis();
                                                                                                        Logger.logsError(TAG, "End Time ms : " + endTime);

                                                                                                        String endDateTime = year + "-" +
                                                                                                                String.format("%02d", (monthOfYear + 1)) + "-" +
                                                                                                                String.format("%02d", dayOfMonth) + " " +
                                                                                                                String.format("%02d", hourOfDay) + ":" +
                                                                                                                String.format("%02d", minute); /*+ ":" +
                                                                                                                String.format("%02d", second);*/
                                                                                                        enddatetime_et.setText(endDateTime);


                                                                                                    }
                                                                                                }, now.get(Calendar.HOUR_OF_DAY)
                                , now.get(Calendar.MINUTE)
                                , now.get(Calendar.SECOND), false);

                        mTimePickerDialog.setTitle(getResources().getString(R.string.enddatetime));
                        mTimePickerDialog.setAccentColor(ContextCompat.getColor(getActivity(), R.color.yellow));


                        Calendar mSelecteDateCal = Calendar.getInstance();
                        mSelecteDateCal.set(year, (monthOfYear), dayOfMonth);

                        Date mCurrentDate = new Date(System.currentTimeMillis());

                        Date mSelectedData  = new Date(mSelecteDateCal.getTimeInMillis());

                        int result = mCurrentDate.compareTo(mSelectedData);
                        /**result will be an int < 0 if now Date is less than the theOtherDate Date, 0 if they are equal, and an int > 0 if this Date is greater.*/


                        if (result>=0){
                            Timepoint mTimepoint = new Timepoint(now.get(Calendar.HOUR_OF_DAY)
                                    , now.get(Calendar.MINUTE)
                                    , now.get(Calendar.SECOND));
                            mTimePickerDialog.setMinTime(mTimepoint);
                        }
/*
                        Timepoint mTimepoint = new Timepoint(now.get(Calendar.HOUR_OF_DAY)
                                , now.get(Calendar.MINUTE)
                                , now.get(Calendar.SECOND));
                        mTimePickerDialog.setMinTime(mTimepoint);*/
                        mTimePickerDialog.show(getActivity().getFragmentManager(), "Timepickerdialog");

                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setTitle(getResources().getString(R.string.enddatetime));
        dpd.setAccentColor(ContextCompat.getColor(getActivity(), R.color.yellow));
        final Calendar mCurrentTIme= Calendar.getInstance();

        dpd.setMinDate(mCurrentTIme);
/*
        Calendar minDateCal = Calendar.getInstance();
        minDateCal.add(Calendar.HOUR,1);
        dpd.setMinDate(minDateCal);*/


        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }


    @Override
    public void clickToLocation() {
        if (validateData()) {
            mEventDatum.setName(event_name_et.getText().toString().trim());
            mEventDatum.setDescription(descrip_et.getText().toString().trim());
            mEventDatum.setStartingDate(startTime);
//            mEventDatum.setDoorsOpeningHour(CommonUtils.getHHMMSS(startTime));

            if (endTime == null || endTime == 0) {
                Date a = new Date();
                a.setTime(startTime + (1 * 24 * 60 * 60 * 1000)); // 1day
                endTime = a.getTime();


            }

            mEventDatum.setEndingDate(endTime);

//            mEventDatum.setDoorsClosingHour(CommonUtils.getHHMMSS(endTime));

            Intent intent = new Intent(getActivity(), AddEventProfileActivity.class);
            intent.putExtra(Constant.fromGenralInfo, true);
            intent.putExtra(Constant.EVENT_DATUM, mEventDatum);
            getActivity().startActivity(intent);
            startActivitySideWiseAnimation();

        }

    }

    private boolean validateData() {
        if (event_name_et.getText().toString().trim().isEmpty()) {
            onError(R.string.pleaseEnterEventNameText);
            return false;
        }

        if (descrip_et.getText().toString().trim().isEmpty()) {
            onError(R.string.pleaseEnterEventDescriptionText);
            return false;
        }

        if (startTime == null || startTime == 0) {
            onError(R.string.pleaseChooseStartTimeText);
            return false;
        }

        if (endTime != null) {
            int retval = startTime.compareTo(endTime);


            if (retval == 0 || startingdatetime_et.getText().toString().trim().equalsIgnoreCase(enddatetime_et.getText().toString().trim())) {
                onError(R.string.startTimeShouldBeGreaterThanEndTimeText);
                return false;
            }
            if (retval > 0) {
                onError(R.string.startTimeShouldBeGreaterThanEndTimeText);
                return false;
            }


        }

        return true;
    }

    private void setTypeface() {
        bottomtext_tv.setTypeface(CommonUtils.setRegularFont(getActivity()));
        enddatetime_et.setTypeface(CommonUtils.setRegularFont(getActivity()));
        startingdatetime_et.setTypeface(CommonUtils.setRegularFont(getActivity()));
        datetime_tv.setTypeface(CommonUtils.setRegularFont(getActivity()));
        descrip_et.setTypeface(CommonUtils.setRegularFont(getActivity()));
        event_name_et.setTypeface(CommonUtils.setRegularFont(getActivity()));
        genral_info_tv.setTypeface(CommonUtils.setRegularFont(getActivity()));
        next_btn.setTypeface(CommonUtils.setRegularFont(getActivity()));
    }
}
