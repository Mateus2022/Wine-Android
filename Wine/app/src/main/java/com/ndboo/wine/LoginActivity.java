package com.ndboo.wine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ndboo.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

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

    @OnClick({R.id.iv_back, R.id.tv_login, R.id.tv_register})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_login:
                login(mEtPhone.getText().toString(),mEtPwd.getText().toString());
                break;
            case R.id.tv_register:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
        }

    }

    /**
     * 登录
     * @param phone 账号
     * @param password  密码
     */
    private void login(String phone, String password) {
        ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("正在登陆...");
        dialog.show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {
//        Subscription subscription = RetrofitHelper.getApi()
//                .login("name", "password")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String s) {
//
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                    }
//                });
//        addSubscription(subscription);
    }
}
