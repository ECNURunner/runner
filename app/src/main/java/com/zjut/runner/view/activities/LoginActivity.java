package com.zjut.runner.view.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogInCallback;
import com.zjut.runner.Controller.AsyncTaskController;
import com.zjut.runner.Controller.CurrentSession;
import com.zjut.runner.Model.CampusModel;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.GeneralUtils;
import com.zjut.runner.util.MyPreference;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.util.ToastUtil;

import java.util.List;

/**
 * Created by Phuylai on 2016/10/5.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher,
        TextView.OnEditorActionListener {

    @Override
    protected void initActionBar() {

    }

    public static final String TAG = LoginActivity.class.getSimpleName();

    public static final double ICON_MARGIN_TOP_OFF_SET = 0.099;

    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private ImageView iv_user;
    private ProgressBar pb_login_in_progress;
    private String username = "";
    private String password = "";
    private TextView tv_signup;
    private TextView tv_forgetPassword;
    private TextView tvUsernameDesc;
    private TextView tv_verify;
    private MyPreference preference;

    //campus model
    private CampusModel campusModel;

    //load campus model
    private AsyncTask<Object,Void,CampusModel> dbLoad = null;
    //save campus model
    private AsyncTask<Object,Void,Void> dbSave = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        layoutId = R.layout.activity_login;
        preference = MyPreference.getInstance(this);
        super.onCreate(savedInstanceState);
    }

    protected void changeIconMarginTop() {
        double layoutHeight = GeneralUtils
                .getDisplayLayoutHeightForNoActionBarScreen(this);
        int marginTop = (int) (ICON_MARGIN_TOP_OFF_SET * layoutHeight);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) iv_user.getLayoutParams();
        lp.topMargin = marginTop;
        iv_user.setLayoutParams(lp);
    }

    @Override
    protected void findViews() {
        super.findViews();
        super.findViews();
        tvUsernameDesc = (TextView) findViewById(R.id.tv_username_desc);
        tv_signup = (TextView) findViewById(R.id.tv_signup);
        tv_forgetPassword = (TextView) findViewById(R.id.tv_forget_password);
        tv_verify = (TextView) findViewById(R.id.tv_verify);
        iv_user = (ImageView) findViewById(R.id.iv_user);
        pb_login_in_progress = (ProgressBar) findViewById(R.id.pb_login_in_progress);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        username = preference.getUsername();
        password = preference.getPassword();
        et_username.setText(username);
        et_password.setText(password);
        setLogin();
        GeneralUtils.setPassWordEditTextHintType(et_password);
        setButtonDisable();
        changeIconMarginTop();
    }

    private void setLogin() {
        et_username.setHint(R.string.str_login_phone);
        tvUsernameDesc.setText("");
        et_username.setInputType(InputType.TYPE_CLASS_TEXT);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tvUsernameDesc.getLayoutParams();
        lp.leftMargin = GeneralUtils.getDimenPx(this, R.dimen.large_margin);
        tvUsernameDesc.setLayoutParams(lp);
    }

    private void setButtonDisable() {
        if (checkInputInfo()) {
            btn_login.setEnabled(true);
            btn_login.getBackground().setAlpha(Constants.DEF_OPAQUE);
        } else {
            btn_login.setEnabled(false);
            btn_login.getBackground().setAlpha(Constants.DISABLE_OPAQUE);
        }
    }

    private boolean checkInputInfo() {
        username = et_username.getText().toString();
        password = et_password.getText().toString();

        if (StringUtil.isNull(username)) {
            return false;
        }

        return !StringUtil.isNull(password);
    }

    @Override
    protected void setListeners() {
        //super.setListeners();
        tv_signup.setOnClickListener(this);
        tv_verify.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        et_username.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
        et_password.setOnEditorActionListener(this);
        tv_forgetPassword.setOnClickListener(this);
    }

    private boolean enableLogIn() {
        if (pb_login_in_progress.getVisibility() == View.GONE) {
            return false;
        }

        pb_login_in_progress.setVisibility(View.GONE);
        et_username.setEnabled(true);
        et_password.setEnabled(true);
        setButtonDisable();

        return true;
    }

    protected void startLoading() {
        username = et_username.getText().toString();
        password = et_password.getText().toString();

        if (StringUtil.isNull(username) || StringUtil.isNull(password)) {
            ToastUtil.showToast(R.string.toast_login_null);
        } else if (!GeneralUtils.matchREGEX(Constants.PASSWORD_RULES_REGEX,
                password)) {
            ToastUtil.showToast(R.string.toast_login_invalid_username);
        } else {
            disableLogIn();
            AVUser.loginByMobilePhoneNumberInBackground(username, password, new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    if (e == null) {
                        campusModel = CampusModel.setCampusModel(AVUser.getCurrentUser());
                        if(!StringUtil.isNull(campusModel.getCampusID())){
                            loadCampusInfo();
                        }else{
                            goToMainActivity();
                        }
                    } else {
                        showToast(R.string.toast_login_error);
                        enableLogIn();
                    }
                }
            });
        }
    }

    private void goToMainActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        MyPreference.getInstance(getApplicationContext()).setPassword(password);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAM_CAMPUS,campusModel);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private synchronized void loadCampusInfo(){
        if(dbLoad != null){
            return;
        }
        dbLoad = new AsyncTask<Object, Void, CampusModel>() {
            @Override
            protected CampusModel doInBackground(Object... params) {
                return CurrentSession.getCurrentCampusInfo(getApplicationContext(),campusModel);
            }

            @Override
            protected void onPostExecute(CampusModel campus) {
                dbLoad = null;
                if(campus != null){
                    campusModel = campus;
                }
                loadFromCloud(campusModel.getCampusID());
            }
        };
        AsyncTaskController.startTask(dbLoad);
    }

    private void loadFromCloud(final String campusID){
        AVQuery<AVObject> query = new AVQuery<AVObject>(Constants.TABLE_CAMPUS);
        query.setCachePolicy(AVQuery.CachePolicy.IGNORE_CACHE);
        query.whereEqualTo(Constants.PARAM_ID, campusID);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null && list.size() > 0) {
                    campusModel = CampusModel.refreshCampus(campusModel,list.get(0));
                    saveCampusModelToDB();
                } else {
                    ToastUtil.showToastShort(getApplicationContext(), R.string.connection_not_avalible);
                    goToMainActivity();
                }
            }
        });
    }

    private synchronized void saveCampusModelToDB() {
        if (dbSave != null) {
            return;
        }
        dbSave = new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... params) {
                CurrentSession.putCampusModelwithCache(getApplicationContext(),campusModel);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                dbSave = null;
                goToMainActivity();
            }
        };
        AsyncTaskController.startTask(dbSave);
    }

    private void disableLogIn() {
        pb_login_in_progress.setVisibility(View.VISIBLE);

        et_username.setEnabled(false);
        et_password.setEnabled(false);
        btn_login.setEnabled(false);
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_login:
                startLoading();
                break;
            case R.id.tv_signup:
                intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent,
                        RegisterActivity.REQUEST_REGISTER_CODE);
                overridePendingTransition(R.animator.back_in,
                        R.animator.back_out);
                break;
            case R.id.tv_forget_password:
                intent = new Intent(this, ResetPasswordActivity.class);
                startActivityForResult(intent,
                        RegisterActivity.REQUEST_REGISTER_CODE);
                overridePendingTransition(R.animator.back_in,
                        R.animator.back_out);
                break;
            case R.id.tv_verify:
                intent = new Intent(this,VerifyActivity.class);
                startActivityForResult(intent,
                        RegisterActivity.REQUEST_REGISTER_CODE);
                overridePendingTransition(R.animator.back_in,
                        R.animator.back_out);
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setButtonDisable();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.et_password) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_DONE:
                    startLoading();
                    return true;
                default:
                    break;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RegisterActivity.REQUEST_REGISTER_CODE
                && resultCode == RegisterActivity.RESULT_REGISTER_SUCCESS) {
            String userName = preference.getUsername();
            String passWord = preference.getPassword();
            et_username.setText(userName);
            et_password.setText(passWord);
        }
    }
}
