package com.zjut.runner.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.widget.MaterialDialog;

import java.util.Calendar;

/**
 * Created by Phuylai on 2016/10/24.
 */

public class NewRequestFragment extends BaseFragment implements View.OnFocusChangeListener, View.OnClickListener,
        TextWatcher, SeekBar.OnSeekBarChangeListener {

    protected EditText et_title;
    protected EditText et_time;
    protected EditText et_deadline;
    protected EditText et_remarks;
    protected EditText et_dest;
    protected TextView tv_charge;
    protected SeekBar seekBar;
    protected Button bt_submit;
    private View mView;
    protected ProgressBar progressBar;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    private String remark = "";
    private String dest = "";
    private String title = "";
    private int charge = 5;
    private String deadline = "";
    private String time;
    private String campusObjectID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        campusObjectID = activity.campusModel.getObjectId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutId = R.layout.fragment_create_new;
        if(mView != null){
            ViewGroup parent = (ViewGroup) mView.getParent();
            if(parent != null){
                parent.removeView(mView);
            }
        }else{
            mView = inflater.inflate(layoutId,null);
        }
        findViews(mView);
        return mView;
    }

    private void successSubmit() {
        final MaterialDialog materialDialog = new MaterialDialog(activity);
        materialDialog.setTitle(R.string.str_sucess);
        materialDialog.setMessage(R.string.submit);
        materialDialog.setPositiveButton(R.string.button_ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                setEnable(true);
                //TODO: clear input
                clearInput();
            }
        });
        materialDialog.show();
    }

    private void clearInput(){
        et_title.setText("");
        et_dest.setText("");
        seekBar.setProgress(5);
        tv_charge.setText(getString(R.string.str_charge,"5 "));
        et_remarks.setText("");
        charge = 5;
        //updateCurrentTime();
    }

    private void failSubmit() {
        final MaterialDialog materialDialog = new MaterialDialog(activity);
        materialDialog.setTitle(R.string.str_fail);
        materialDialog.setMessage(R.string.fail_submit);
        materialDialog.setPositiveButton(R.string.button_ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                setEnable(true);
            }
        });
        materialDialog.show();
    }

    protected void setEnable(boolean b) {
        et_time.setEnabled(b);
        et_remarks.setEnabled(b);
        bt_submit.setEnabled(b);
        et_deadline.setEnabled(b);
        et_title.setEnabled(b);
        et_dest.setEnabled(b);
        seekBar.setEnabled(b);
    }

    @Override
    public void changeTitle() {
        activity.changeTitle(R.string.title_new);
    }

    @Override
    protected void findViews(View rootView) {
        activity.expandToolbar(false);
        et_title = (EditText) rootView.findViewById(R.id.et_title);
        et_deadline = (EditText) rootView.findViewById(R.id.et_deadline);
        et_dest = (EditText) rootView.findViewById(R.id.et_dest);
        tv_charge = (TextView) rootView.findViewById(R.id.tv_charge);
        seekBar = (SeekBar) rootView.findViewById(R.id.seekbar);
        et_time = (EditText) rootView.findViewById(R.id.et_time);
        et_remarks = (EditText) rootView.findViewById(R.id.et_remark);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pb_sending_post);
        progressBar.setVisibility(View.GONE);
        bt_submit = (Button) rootView.findViewById(R.id.btn_sub);
        tv_charge.setText(getString(R.string.str_charge,"5 ")+ getString(R.string.str_currency));
        //updateCurrentTime();
        setButtonDisable(checkInputInfo());
    }

    protected void updateCurrentTime() {
        getTimeNow();
        et_time.setText(getTimeString(year, month, day, hour, minute));
    }

    private void getTimeNow() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
    }

    @Override
    protected void setListener() {
        et_time.setOnFocusChangeListener(this);
        et_time.setOnClickListener(this);
        et_deadline.setOnFocusChangeListener(this);
        et_deadline.setOnClickListener(this);
        et_dest.addTextChangedListener(this);
        et_title.addTextChangedListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        et_time.addTextChangedListener(this);
        et_deadline.addTextChangedListener(this);
        et_remarks.addTextChangedListener(this);
        //et_remarks.requestFocus();
        bt_submit.setOnClickListener(this);
    }

    @Override
    public void onSearchClose() {

    }

    @Override
    public void search(String searchString) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            if(v.getId() == R.id.et_time) {
                showDateTimePicker(et_time);
            }else{
                showDateTimePicker(et_deadline);
            }
        }
        et_dest.requestFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_time:
                showDateTimePicker(et_time);
                break;
            case R.id.et_deadline:
                showDateTimePicker(et_deadline);
                break;
            case R.id.btn_sub:
                submitRequest();
                break;
        }
    }

    private void showDateTimePicker(final EditText editText) {
        activity.hideKeyBoard();
        final View view = activity.getLayoutInflater().inflate(R.layout.frame_date_time, null);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datepicker);
        final TimePicker timePicker = (TimePicker) view.findViewById(R.id.timepicker);
        getTimeNow();
        datePicker.setMinDate(System.currentTimeMillis() - 1000);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                NewRequestFragment.this.year = year;
                NewRequestFragment.this.month = monthOfYear;
                NewRequestFragment.this.day = dayOfMonth;
            }
        });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                NewRequestFragment.this.hour = hourOfDay;
                NewRequestFragment.this.minute = minute;
            }
        });
        final MaterialDialog materialDialog = new MaterialDialog(activity)
                .setTitle("Date and Time")
                .setView(view);
        materialDialog.setPositiveButton(getString(R.string.str_set), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timePicker.getVisibility() == View.VISIBLE){
                    editText.setText(getTimeString(year,month,day,hour,minute));
                    materialDialog.dismiss();
                }else {
                    timePicker.setVisibility(View.VISIBLE);
                    datePicker.setVisibility(View.GONE);
                    materialDialog.setView(view);
                }
            }
        });
        materialDialog.setNegativeButton(getString(R.string.button_cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
        materialDialog.show();
    }

    private String getTimeString(int year,int month,int day,int hour,int minute){
        return year + "-" + (month + 1) + "-" + day + "  " +
                StringUtil.twoDigit(hour) + ":" + StringUtil.twoDigit(minute);
    }

    private void submitRequest(){
        final MaterialDialog materialDialog = new MaterialDialog(activity)
                .setTitle(getString(R.string.reminder))
                .setMessage(R.string.str_sure)
                .setCanceledOnTouchOutside(false);
        materialDialog.setPositiveButton(getString(R.string.button_ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
                progressBar.setVisibility(View.VISIBLE);
                setEnable(false);
                remark = String.valueOf(et_remarks.getText());
                time = String.valueOf(et_time.getText());
                deadline = String.valueOf(et_deadline.getText());
                title = String.valueOf(et_title.getText());
                dest = String.valueOf(et_dest.getText());
                //UPDATE TO CLOUD
                updateToCloud(time, remark, deadline,title,dest,charge);
            }
        });
        materialDialog.setNegativeButton(getString(R.string.button_cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
        materialDialog.show();
    }

    private void updateToCloud(final String time, final String remark, final String deadline, final String title,
                               final String dest, final int charge) {
        AVObject campusObject = AVObject.createWithoutData(Constants.TABLE_CAMPUS,campusObjectID);
        campusObject.fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if(e == null){
                    putRequest(avObject,time,remark,deadline,title,dest,charge);
                }else{
                    failSubmit();
                }
            }
        });
    }

    private void putRequest(final AVObject campus, String time, String remark, String deadline, String title, String dest, int charge){
        final AVObject repair = new AVObject(Constants.TABLE_REQUEST);
        repair.put(Constants.PARAM_REMARK, remark);
        repair.put(Constants.PARAM_ORDER_DATE, time);
        repair.put(Constants.PARAM_DEADLINE, deadline);
        repair.put(Constants.PARAM_TITLE, title);
        repair.put(Constants.PARAM_DEST, dest);
        repair.put(Constants.PARAM_CHARGE, charge);
        repair.put(Constants.PARAM_CAMPUS_ID, activity.campusModel.getCampusID());
        repair.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    //successSubmit();
                    putToReplyRequest(repair.getObjectId(),campus);
                } else {
                    failSubmit();
                }
            }
        });
    }

    private void putToReplyRequest(String objectID,final AVObject campus){
        AVObject request = AVObject.createWithoutData(Constants.TABLE_REQUEST,objectID);
        request.fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if(e == null){
                    AVObject reply = new AVObject(Constants.PARAM_REQUEST_REPLY);
                    reply.put(Constants.PARAM_REQUEST_OBJ,avObject);
                    reply.put(Constants.PARAM_OWNER_CAMPUS,campus);
                    reply.put(Constants.PARAM_OWNER_USER, AVUser.getCurrentUser());
                    reply.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if(e == null){
                                successSubmit();
                            }else{
                                failSubmit();
                            }
                        }
                    });
                }else{
                    failSubmit();
                }
            }
        });
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setButtonDisable(checkInputInfo());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    protected void setButtonDisable(boolean checkInputInfo) {
        if (checkInputInfo) {
            bt_submit.setEnabled(true);
            bt_submit.getBackground().setAlpha(Constants.DEF_OPAQUE);
        } else {
            bt_submit.setEnabled(false);
            bt_submit.getBackground().setAlpha(Constants.DISABLE_OPAQUE);
        }
    }

    protected boolean checkInputInfo() {
        String time = et_time.getText().toString();
        String remark = et_remarks.getText().toString();
        String title = et_title.getText().toString();
        String dest = et_dest.getText().toString();
        String deadline = et_deadline.getText().toString();
        if(StringUtil.isNull(time) || StringUtil.isNull(title) || StringUtil.isNull(dest)
                || StringUtil.isNull(deadline)){
            return false;
        }
        return !StringUtil.isNull(remark);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        charge = progress;
        tv_charge.setText(getString(R.string.str_charge,String.valueOf(progress))+ " " +
                getString(R.string.str_currency));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

