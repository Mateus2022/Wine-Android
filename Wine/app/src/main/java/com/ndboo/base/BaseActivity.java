package com.ndboo.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ndboo.wine.R;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Li on 2016/12/23.
 * Activity基类
 */

public abstract class BaseActivity extends AppCompatActivity {


    private CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * API大于21（Android5.0）时设置标题栏的颜色
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(getResources().getColor(R.color.themeColor));
        }
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unSubscribe();
    }

    /**
     * 添加事件订阅
     * @param subscription  订阅
     */
    protected void addSubscription(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        if (subscription != null) {
            mCompositeSubscription.add(subscription);
        }
    }


    /**
     * 取消所有事件订阅
     */
    protected void unSubscribe(){
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    /**
     * 获取布局Id
     * @return  布局Id
     */
    public abstract int getLayoutId();

    public abstract void init();
}
