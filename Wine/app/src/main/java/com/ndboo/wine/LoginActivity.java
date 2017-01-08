package com.ndboo.wine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ndboo.base.BaseActivity;
import com.ndboo.net.RetrofitHelper;
import com.ndboo.utils.SharedPreferencesUtil;
import com.ndboo.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;

    @BindView(R.id.tv_login)
    TextView mTvLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    private ProgressDialog mProgressDialog;

    @OnClick({R.id.iv_back, R.id.tv_login, R.id.tv_register})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_login:
                login(mEtPhone.getText().toString(), mEtPwd.getText().toString());
                break;
            case R.id.tv_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }

    }

    /**
     * 登录
     *
     * @param phone    账号
     * @param password 密码
     */
    private void login(final String phone, String password) {


        mProgressDialog.show();
        Subscription subscription = RetrofitHelper.getApi()
                .loginByPassword(phone, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mProgressDialog.cancel();
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            String memberId=jsonObject.getString("memberId");
                            String status=jsonObject.getString("loginStatus");
                            if (status.equals("2")) {
                                SharedPreferencesUtil.saveUserInfo(LoginActivity.this,memberId,phone);
                                finish();
                            }else {
                                ToastUtil.showToast(LoginActivity.this,"用户名或密码不正确");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mProgressDialog.cancel();
                        ToastUtil.showToast(LoginActivity.this,"网络连接错误");
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在登陆...");
        mProgressDialog.setCancelable(false);
    }
}
