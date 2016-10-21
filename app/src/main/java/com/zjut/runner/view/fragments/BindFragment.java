package com.zjut.runner.view.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.zjut.runner.Controller.AsyncTaskController;
import com.zjut.runner.Controller.CurrentSession;
import com.zjut.runner.Model.ActionType;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.util.ToastUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */

public class BindFragment extends BaseFragment implements View.OnClickListener, TextWatcher {

    protected EditText inputID;
    protected EditText inputPass;
    protected EditText inputOldPass;
    protected ProgressBar progressBar;
    protected String defValue;
    protected ActionType actionType;
    protected TextView tv_warning;
    protected Button btn_submit;

    //save campus model
    private AsyncTask<Object,Void,Void> dbSave = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            defValue = bundle.getString(Constants.PARAM_VALUE);
            actionType = ActionType.getType(bundle.getString(Constants.PARAM_ACTION));
            if( defValue != null && getString(R.string.str_not_set).equals(defValue)){
                defValue = "";
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutId = R.layout.fragment_profile_edit;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void changeTitle() {
        setTitle(R.string.str_bind);
    }

    @Override
    protected void findViews(View rootView) {
        tv_warning = (TextView) rootView.findViewById(R.id.tv_warning);
        inputID = (EditText) rootView.findViewById(R.id.et_input);
        inputPass = (EditText) rootView.findViewById(R.id.et_pass);
        inputOldPass = (EditText) rootView.findViewById(R.id.et_old_pass);
        btn_submit = (Button) rootView.findViewById(R.id.btn_verificationBtn);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pb_sending_post);
        inputID.setHint(R.string.hint_card_no);
        inputPass.setHint(R.string.hint_card_pass);
        //inputPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
        setActionButtonDisable(checkInputInfo());
    }

    protected void setEnable(boolean enable){
        inputID.setEnabled(enable);
        inputPass.setEnabled(enable);
        btn_submit.setEnabled(enable);
    }

    protected boolean checkInputInfo() {
        String first = inputID.getText().toString();
        String second = inputPass.getText().toString();

        if (StringUtil.isNull(first)) {
            return false;
        }
        return !StringUtil.isNull(second);
    }

    protected void setActionButtonDisable(boolean isEnable) {
        btn_submit.setEnabled(isEnable);
        if (isEnable) {
            btn_submit.getBackground().setAlpha(Constants.DEF_OPAQUE);
        } else {
            btn_submit.getBackground().setAlpha(
                    Constants.DISABLE_OPAQUE);
        }
    }

    @Override
    protected void setListener() {
        btn_submit.setOnClickListener(this);
        inputID.addTextChangedListener(this);
        inputPass.addTextChangedListener(this);
        inputOldPass.addTextChangedListener(this);
    }

    @Override
    public void onSearchClose() {

    }

    @Override
    public void search(String searchString) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_verificationBtn:
                progressBar.setVisibility(View.VISIBLE);
                setEnable(false);
                final String id = inputID.getText().toString();
                final String passWord = inputPass.getText().toString();
                AVQuery<AVObject> query = new AVQuery<AVObject>(Constants.TABLE_CAMPUS);
                query.whereEqualTo(Constants.PARAM_ID,id);
                query.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (e == null) {
                            queryTable(list,passWord,id);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            setEnable(true);
                            ToastUtil.showToastShort(activity, R.string.connection_not_avalible);
                        }
                    }
                });
                break;
        }
    }

    private void queryTable(List<AVObject> list, String passWord, String id) {
        if(list.size() > 0){
            final AVObject avObject = list.get(0);
            String pass = avObject.getString(Constants.PARAM_CARD_PASS);
            if(passWord.equals(pass)){
                AVUser.getCurrentUser().setFetchWhenSave(true);
                AVUser.getCurrentUser().put(Constants.PARAM_ID,id);
                AVUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e == null){
                            saveDataToCampusModel(avObject);
                        }
                    }
                });
            }else{
                progressBar.setVisibility(View.GONE);
                setEnable(true);
                ToastUtil.showToastShort(activity,R.string.toast_fail_bind);
            }
        }else{
            setEnable(true);
            ToastUtil.showToastShort(activity,R.string.toast_fail_bind);
        }
    }

    private void saveDataToCampusModel(AVObject avObject) {
        activity.campusModel.setCampusID(avObject.getString(Constants.PARAM_ID));
        activity.campusModel.setCardPass(avObject.getString(Constants.PARAM_CARD_PASS));
        activity.campusModel.setCampusName(avObject.getString(Constants.PARAM_CARD_NAME));
        activity.campusModel.setBalance(avObject.getInt(Constants.PARAM_CARD_BALANCE));
        saveCampusModelToDB();
    }

    private synchronized void saveCampusModelToDB() {
        if (dbSave != null) {
            return;
        }
        dbSave = new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... params) {
                CurrentSession.putCampusModelwithCache(activity, activity.campusModel);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                progressBar.setVisibility(View.GONE);
                ToastUtil.showToastShort(activity, R.string.toast_bind);
                dbSave = null;
                getFragmentManager().popBackStack();
            }
        };
        AsyncTaskController.startTask(dbSave);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setActionButtonDisable(checkInputInfo());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
