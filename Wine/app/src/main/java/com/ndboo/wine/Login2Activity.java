package com.ndboo.wine;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.ndboo.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class Login2Activity extends BaseActivity {
    @BindView(R.id.login2_account)
    EditText mAccountEditText;//账号
    @BindView(R.id.login2_password)
    EditText mPasswordEditText;//密码
    @BindView(R.id.login2_password_switch)
    Switch mSwitch;//密码显示与隐藏

    @OnClick({R.id.login2_aggrement, R.id.login2_login, R.id.login2_forget,
            R.id.login2_register})
    void doClick(View view) {
        switch (view.getId()) {
            case R.id.login2_aggrement:
                //用户协议
                break;
            case R.id.login2_login:
                //登录
                break;
            case R.id.login2_forget:
                //忘记密码
                break;
            case R.id.login2_register:
                //注册
                break;
        }
    }

    @Override
    public int getLayoutId() {
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, flag);
        return R.layout.activity_login2;
    }

    @Override
    public void init() {
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }
}
