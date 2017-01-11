package com.ndboo.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ndboo.base.BaseFragment;
import com.ndboo.bean.WineBean;
import com.ndboo.interfaces.NumOperationListener;
import com.ndboo.net.RetrofitHelper;
import com.ndboo.utils.SharedPreferencesUtil;
import com.ndboo.utils.ToastUtil;
import com.ndboo.widget.NumOperationView;
import com.ndboo.wine.LoginActivity;
import com.ndboo.wine.MainActivity;
import com.ndboo.wine.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Li on 2017/1/5.
 * “酒”适配器
 */

public class WineAdapter extends BaseAdapter {
    private Context mContext;
    private List<WineBean> mWines;
    private BaseFragment mBaseFragment;

    public WineAdapter(Context context, BaseFragment baseFragment) {
        mContext = context;
        mMainActivity = (MainActivity) mContext;
        mBaseFragment = baseFragment;
        if (mWines != null) {
            mWines = new ArrayList<>();
        }
    }

    private MainActivity mMainActivity;

    public void setWines(List<WineBean> wines) {
        mWines = wines;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return mWines == null ? 0 : mWines.size();
    }

    @Override
    public Object getItem(int position) {
        return mWines.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final WineBean wine = mWines.get(position);
        final WineViewHolder wineViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_wine, null);
            wineViewHolder = new WineViewHolder(convertView);
            convertView.setTag(wineViewHolder);
        } else {
            wineViewHolder = (WineViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(wine.getPicPath()).into(wineViewHolder.mIv);
        wineViewHolder.mTvWineName.setText(wine.getProductName());
        wineViewHolder.mTvPrice.setText("¥" + wine.getProductPrice());
        wineViewHolder.mViewNumOperation.setNumber(wine.getCartProductCount());
        wineViewHolder.mViewNumOperation.setNumOperationListener(new NumOperationListener() {
            @Override
            public void numAdd(final int position, final View view) {
                view.setEnabled(false);
                if (SharedPreferencesUtil.isUserLoginIn(mContext)) {
                    //数量为0时，调用添加接口
                    if (mWines.get(position).getCartProductCount().equals("0")) {
                        Subscription addSubscription = RetrofitHelper.getApi()
                                .addWineToCar(SharedPreferencesUtil.getUserId(mContext), wine.getProductId(), "1")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        mMainActivity.queryWineNum();
                                        view.setEnabled(true);
                                        wineViewHolder.mViewNumOperation.numAdd();
                                        mWines.get(position).setCartProductCount(wineViewHolder.mViewNumOperation.getNumber());
                                        notifyDataSetChanged();
                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        view.setEnabled(true);
                                    }
                                });

                        mBaseFragment.addSubscription(addSubscription);
                    } else {
                        Subscription subscription = RetrofitHelper.getApi()
                                .modifyProductNum(SharedPreferencesUtil.getUserId(mContext), wine.getProductId(),
                                        Integer.parseInt(wine.getCartProductCount()) + 1 + "")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        view.setEnabled(true);

                                        wineViewHolder.mViewNumOperation.numAdd();
                                        mWines.get(position).setCartProductCount(wineViewHolder.mViewNumOperation.getNumber());
                                        notifyDataSetChanged();
                                        Log.e("tag", s);
                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        view.setEnabled(true);
                                        ToastUtil.showToast(mBaseFragment.getActivity(), "网络连接错误");
                                    }
                                });
                        mBaseFragment.addSubscription(subscription);
                    }


                } else {
                    view.setEnabled(true);
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                }

            }

            @Override
            public void numReduce(final int position, final View view) {
                view.setEnabled(false);
                if (SharedPreferencesUtil.isUserLoginIn(mContext)) {

                    if (wine.getCartProductCount().equals("1")) {
                        Subscription reduceSub = RetrofitHelper.getApi()
                                .deleteFromCart(SharedPreferencesUtil.getUserId(mContext), wine.getProductId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        view.setEnabled(true);
                                        mMainActivity.queryWineNum();
                                        wineViewHolder.mViewNumOperation.numReduce();
                                        mWines.get(position).setCartProductCount(wineViewHolder.mViewNumOperation.getNumber());
                                        notifyDataSetChanged();
                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        view.setEnabled(true);
                                    }
                                });
                        mBaseFragment.addSubscription(reduceSub);
                    } else {
                        Subscription subscription = RetrofitHelper.getApi()
                                .modifyProductNum(SharedPreferencesUtil.getUserId(mContext), wine.getProductId(),
                                        Integer.parseInt(wine.getCartProductCount()) - 1 + "")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        view.setEnabled(true);
                                        wineViewHolder.mViewNumOperation.numReduce();
                                        mWines.get(position).setCartProductCount(wineViewHolder.mViewNumOperation.getNumber());
                                        notifyDataSetChanged();
                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        view.setEnabled(true);
                                        Log.e("tag", throwable.getMessage());
                                        ToastUtil.showToast(mBaseFragment.getActivity(), "网络连接错误");
                                    }
                                });
                        mBaseFragment.addSubscription(subscription);
                    }


                } else {
                    view.setEnabled(true);
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                }


            }

            @Override
            public int position() {
                return pos;
            }
        });
        return convertView;
    }


    static class WineViewHolder {
        @BindView(R.id.iv)
        ImageView mIv;
        @BindView(R.id.tv_price)
        TextView mTvPrice;
        @BindView(R.id.view_num_operation)
        NumOperationView mViewNumOperation;
        @BindView(R.id.tv_wine_name)
        TextView mTvWineName;

        WineViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
