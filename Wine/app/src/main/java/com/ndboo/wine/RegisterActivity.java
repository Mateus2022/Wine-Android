package com.ndboo.wine;

import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ndboo.base.BaseActivity;
import com.ndboo.net.RetrofitHelper;
import com.ndboo.utils.ToastUtil;
import com.ndboo.utils.VerificationUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 注册界面
 */
public class RegisterActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    //账号输入框
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    //验证码输入框
    @BindView(R.id.et_code)
    EditText mEtCode;
    //获取验证码按钮
    @BindView(R.id.tv_get_code)
    TextView mTvGetCode;
    //下一步按钮
    @BindView(R.id.tv_next_step)
    TextView mTvNextStep;
    //第一步布局
    @BindView(R.id.layout_step_1)
    RelativeLayout mLayoutStep1;
    //密码输入框
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    //确认密码输入框
    @BindView(R.id.et_confirm_pwd)
    EditText mEtConfirmPwd;
    //注册按钮
    @BindView(R.id.tv_register)
    TextView mTvRegister;
    //第二步布局
    @BindView(R.id.layout_step_2)
    RelativeLayout mLayoutStep2;

    private String smsKey;
    private String smsSecret;

    private String phone;
    private EventHandler eh;
    private RelativeLayout mCurrentLayout;
    private ProgressDialog mProgressDialog;

    @OnClick({R.id.iv_back, R.id.tv_get_code, R.id.tv_next_step, R.id.tv_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                back();
                break;
            case R.id.tv_get_code:
                getCodes();
                break;
            case R.id.tv_next_step:
                submitCode(phone, mEtCode.getText().toString());
                break;
            case R.id.tv_register:
                String pwd = mEtPwd.getText().toString();
                String confirmPwd = mEtConfirmPwd.getText().toString();

                if (pwd.equals(confirmPwd) && pwd.length() >= 6 && pwd.length() <= 15) {
                    //验证成功，向后台请求注册返回结果
                    register(phone, pwd);
                } else {
                    showErrorDialog("请确保两次输入内容一样，并且长度在6-15个字符之内");
                }
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }
private String mType="";
    @Override
    public void init() {
        if (getIntent().getExtras()!=null) {
            mType=getIntent().getExtras().getString("type");
        }
        if (mType.equals("reset")) {
            mTvTitle.setText("重置密码");
        }else {
            mTvTitle.setText("注册");
        }
        VerificationUtil.editTextNoSpace(mEtPhone);
        VerificationUtil.editTextNoSpace(mEtCode);
        VerificationUtil.editTextNoSpace(mEtPwd);
        VerificationUtil.editTextNoSpace(mEtConfirmPwd);

        mEtPhone.addTextChangedListener(this);
        mEtCode.addTextChangedListener(this);
        mEtPwd.addTextChangedListener(this);
        mEtConfirmPwd.addTextChangedListener(this);
        mCurrentLayout = mLayoutStep1;

        try {
            ApplicationInfo applicationInfo = getPackageManager()
                    .getApplicationInfo(getPackageName(),
                            PackageManager.GET_META_DATA);
            smsKey = applicationInfo.metaData.getString("sms_key");
            smsSecret = applicationInfo.metaData.getString("sms_secret");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        SMSSDK.initSDK(getApplicationContext(), smsKey, smsSecret);
        eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object o) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {

                    if (result == SMSSDK.RESULT_COMPLETE) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTvNextStep.setEnabled(true);
                                //验证成功
                                mLayoutStep2.setVisibility(View.VISIBLE);
                                mLayoutStep1.setVisibility(View.GONE);

                                mEtPhone.setText("");
                                mEtCode.setText("");
                                mCurrentLayout = mLayoutStep2;
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //验证失败
                                showErrorDialog("手机号和验证码不匹配，请重新确认");

                            }
                        });
                    }
                }
            }
        };
        SMSSDK.registerEventHandler(eh);

    }

    private void showErrorDialog(String message) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(RegisterActivity.this);
        mBuilder.setTitle("提示")
                .setMessage(message)
                .setPositiveButton("确定", null)
                .create()
                .show();

    }

    /**
     * 获取验证码
     */
    private void getCodes() {
        phone = mEtPhone.getText().toString();
        String phoneCheck = "[1][34578]\\d{9}";
        if (!phone.matches(phoneCheck) || phone.equals("")) {
            showErrorDialog("请输入正确手机号");
            return;
        }

        CodeCountDown codeCountDown = new CodeCountDown(60 * 1000, 1000);
        codeCountDown.start();
        //根据手机号获取验证码
        SMSSDK.getVerificationCode("86", phone, new OnSendMessageHandler() {
            @Override
            public boolean onSendMessage(String s, String s1) {
                return false;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);

    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void submitCode(String phone, String code) {
        mTvNextStep.setEnabled(false);
        //提交手机号和验证码
        SMSSDK.submitVerificationCode("86", phone, code);
    }

    private void back() {
        if (mLayoutStep1 == mCurrentLayout) {
            finish();
        } else {
            mCurrentLayout = mLayoutStep1;
            mLayoutStep2.setVisibility(View.GONE);
            mLayoutStep1.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(mEtPhone.getText().toString()) ||
                TextUtils.isEmpty(mEtCode.getText().toString())) {
            mTvNextStep.setEnabled(false);
        }

        if (!TextUtils.isEmpty(mEtPhone.getText().toString()) &&
                !TextUtils.isEmpty(mEtCode.getText().toString())) {
            mTvNextStep.setEnabled(true);
        }

        if (TextUtils.isEmpty(mEtPwd.getText().toString()) ||
                TextUtils.isEmpty(mEtConfirmPwd.getText().toString())) {
            mTvRegister.setEnabled(false);
        }

        if (!TextUtils.isEmpty(mEtPwd.getText().toString()) &&
                !TextUtils.isEmpty(mEtConfirmPwd.getText().toString())) {
            mTvRegister.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 注册
     *
     * @param phone 电话号
     * @param pwd   密码
     */
    private void register(String phone, String pwd) {
        mProgressDialog = new ProgressDialog(RegisterActivity.this);
        mProgressDialog.setCancelable(false);
        if (mType.equals("reset")) {
            mProgressDialog.setMessage("重置密码");
            mProgressDialog.show();
            Subscription subscription=RetrofitHelper.getApi()
                    .resetPassword(phone,pwd)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            mProgressDialog.cancel();
                            JSONObject object;
                            try {
                                object = new JSONObject(s);
                                String result = object.getString("result");
                                if (result.equals("success")) {
                                    finish();
                                    ToastUtil.showToast(RegisterActivity.this, "已重置密码");
                                } else {
                                    ToastUtil.showToast(RegisterActivity.this, "重置密码失败");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            mProgressDialog.cancel();
                            ToastUtil.showToast(RegisterActivity.this, "注册失败,请检查网络连接");
                        }
                    });
            addSubscription(subscription);
        }else {
            mProgressDialog.setMessage("正在注册");
            mProgressDialog.show();
            Subscription subscription = RetrofitHelper.getApi()
                    .register(phone, pwd)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            mProgressDialog.cancel();
                            JSONObject object;
                            try {
                                object = new JSONObject(s);
                                String result = object.getString("returnStatus");
                                if (result.equals("success")) {

                                    finish();
                                    ToastUtil.showToast(RegisterActivity.this, "注册成功");
                                } else {
                                    ToastUtil.showToast(RegisterActivity.this, "用户已存在");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            mProgressDialog.cancel();
                            ToastUtil.showToast(RegisterActivity.this, "注册失败,请检查网络连接");
                        }
                    });
            addSubscription(subscription);
        }

    }


    class CodeCountDown extends CountDownTimer {


        CodeCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            mTvGetCode.setText("" + millisUntilFinished / 1000 + "S");
            mTvGetCode.setEnabled(false);
        }

        @Override
        public void onFinish() {
            mTvGetCode.setText("重新获取");
            mTvGetCode.setEnabled(true);
        }
    }
}
