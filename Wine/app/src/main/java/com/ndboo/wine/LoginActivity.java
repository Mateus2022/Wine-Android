package com.ndboo.wine;

import com.ndboo.base.BaseActivity;
import com.ndboo.net.RetrofitHelper;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {
        Subscription subscription = RetrofitHelper.getApi()
                .login("name", "password")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
        addSubscription(subscription);
    }
}
