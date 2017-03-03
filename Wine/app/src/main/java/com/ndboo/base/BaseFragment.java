package com.ndboo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;

/**
 * Created by Li on 2016/12/23.
 * Fragment基类
 */

public abstract class BaseFragment extends Fragment {

    private View mView;
    private Unbinder mUnBinder;
    private boolean flag = true;
    private List<Subscription> mSubscriptions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), null);
        mUnBinder = ButterKnife.bind(this, mView);
        showContent();

        return mView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        if (isVisibleToUser && flag && mUnBinder != null) {
            flag = false;
            firstVisibleDeal();
        }
        if (isVisibleToUser) {
            visibleDeal();
        }
        if (!isVisibleToUser) {
            inVisibleDeal();
        }
    }

    protected void visibleDeal() {
    }

    /**
     * 界面不可见时的处理
     */
    protected void inVisibleDeal() {
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        unSubscribe();
    }

    /**
     * 显示内容
     */
    public void showContent() {
    }

    /**
     * 当界面第一次可见时的操作
     */
    public void firstVisibleDeal() {
    }

    /**
     * 获取当前界面View
     *
     * @return View
     */
    public View getContentView() {

        return mView;
    }

    /**
     * 将订阅添加进集合
     *
     * @param subscription 待添加的订阅
     */
    public void addSubscription(Subscription subscription) {
        if (mSubscriptions == null) {
            mSubscriptions = new ArrayList<>();
        }
        if (subscription != null) {
            mSubscriptions.add(subscription);
        }
    }


    /**
     * 取消订阅
     */
    protected void unSubscribe() {
        if (mSubscriptions != null) {
            for (Subscription subscription : mSubscriptions) {
                if (!subscription.isUnsubscribed()) {
                    subscription.unsubscribe();
                }
            }
        }
    }

    protected abstract int getLayoutId();


}
