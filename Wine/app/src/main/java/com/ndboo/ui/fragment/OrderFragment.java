package com.ndboo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ndboo.adapter.OrderListAdapter;
import com.ndboo.bean.OrderFirstBean;
import com.ndboo.bean.OrderSecondBean;
import com.ndboo.net.RetrofitHelper;
import com.ndboo.utils.SharedPreferencesUtil;
import com.ndboo.utils.ToastUtil;
import com.ndboo.wine.OrderDetailActivity;
import com.ndboo.wine.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by ZhangHang on 2016/9/21.
 */

public class OrderFragment extends Fragment {
    protected static final int TYPE_LOAD = 0;//加载
    protected static final int TYPE_REFRESH = 1;//刷新

    private View mView;


    //当前Fragment是哪个分类
    private int mCurrentSort;

    //当前显示的是第几页
    private int mCurrentPage = 1;

    //列表
    private PtrClassicFrameLayout mPtrClassicFrameLayout;
    private ListView mListView;
    private OrderListAdapter mAdapter;
    //订单的集合
    private List<OrderFirstBean> mFirstBeanList = new ArrayList<>();
    //每个订单中商品的集合
    private Map<Integer, List<OrderSecondBean>> mSecondBeanList = new HashMap<>();

    //标记当前是刷新还是加载,默认刷新
    private int mLoadingType = TYPE_REFRESH;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mCurrentSort = getArguments().getInt("type");
        mView = inflater.inflate(R.layout.fragment_order, null);

        initView();
        addListener();
        return mView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mFirstBeanList.size() == 0) {
            requestData();
        }
    }

    private void initView() {
        mPtrClassicFrameLayout = (PtrClassicFrameLayout) mView.findViewById(R.id.refresh_listview_frame);
        mListView = (ListView) mView.findViewById(R.id.refresh_listview);
        mAdapter = new OrderListAdapter(getActivity(), mSecondBeanList, mFirstBeanList);
        mListView.setAdapter(mAdapter);

        //阻尼系数   默认: 1.7f，越大，感觉下拉时越吃力。
        mPtrClassicFrameLayout.setResistance(1.7f);
        //触发刷新时移动的位置比例  默认，1.2f，移动达到头部高度1.2倍时可触发刷新操作。
        mPtrClassicFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        //回弹延时  默认 200ms，回弹到刷新高度所用时间
        mPtrClassicFrameLayout.setDurationToClose(200);
        //头部回弹时间  默认1000ms
        mPtrClassicFrameLayout.setDurationToCloseHeader(800);
        //刷新时保持头部  默认值 true
        mPtrClassicFrameLayout.setKeepHeaderWhenRefresh(true);
        //下拉刷新 / 释放刷新 默认为释放刷新，即false。
        mPtrClassicFrameLayout.setPullToRefresh(false);
        //模式，支持上拉和下拉
        mPtrClassicFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
        //上次更新时间的key
        mPtrClassicFrameLayout.setLastUpdateTimeHeaderKey("key-" + mCurrentSort);
        //设置头部字体大小
        mPtrClassicFrameLayout.getHeader().setLastUpdateTextSize(13);
        mPtrClassicFrameLayout.getHeader().setTitleTextSize(14);
        mPtrClassicFrameLayout.getFooter().setTitleTextSize(14);
    }

    private void addListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("orderId", mFirstBeanList.get(i).getOrderId());
                startActivity(intent);
            }
        });

        mPtrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                //加载
                mLoadingType = TYPE_LOAD;
                //页数增加
                mCurrentPage++;
                //获取数据
                requestData();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //刷新
                mLoadingType = TYPE_REFRESH;
                //重置为第一页
                mCurrentPage = 1;
                //获取数据
                requestData();
            }
        });
    }

    private void requestData() {
        Subscription subscription = RetrofitHelper.getApi()
                .getOrderByStatus(SharedPreferencesUtil.getUserId(getActivity()), "" + mCurrentSort)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String string) {
                        Log.e("ndb", "result:" + string);
                        try {
                            JSONObject jsonObject = new JSONObject(string);
                            //总价
                            JSONArray jsonArray = jsonObject.optJSONArray("productInformation");
                            int length = jsonArray.length();

                            if (mLoadingType == TYPE_LOAD) {
                                if (length == 0) {
                                    mCurrentPage--;
                                }
                            } else if (mLoadingType == TYPE_REFRESH) {
                                mFirstBeanList.clear();
                                mSecondBeanList.clear();
                            }

                            List<OrderFirstBean> firstList = new ArrayList<>();
                            Map<Integer, List<OrderSecondBean>> secondList = new HashMap<>();
                            for (int i = 0; i < length; i++) {
                                JSONObject object1 = jsonArray.optJSONObject(i);
                                String orderTotal = object1.optString("orderTotal", "");
                                String orderTime = object1.optString("orderTime", "");
                                String orderStatus = object1.optString("orderStatus", "");
                                String orderId = object1.optString("orderId", "");
                                String productTypeCount = object1.optString("productTypeCount", "");
                                JSONArray jsonArray1 = object1.optJSONArray("productInformation");

                                List<OrderSecondBean> secondBeenList = new ArrayList<>();
                                for (int k = 0; k < jsonArray1.length(); k++) {
                                    JSONObject object = jsonArray1.optJSONObject(k);
                                    String productCount = object.optString("productCount", "");
                                    String productName = object.optString("productName", "");
                                    String bazaarPrice = object.optString("bazaarPrice", "");
                                    String productPicture = object.optString("productPicture", "");
                                    String specName = object.optString("specName", "");
                                    String money = object.optString("money", "");

                                    secondBeenList.add(new OrderSecondBean(productPicture, productName,
                                            productCount, bazaarPrice, specName, money));
                                }
                                secondList.put(new Integer(mFirstBeanList.size() + firstList.size()),
                                        secondBeenList);
                                firstList.add(new OrderFirstBean(orderId, orderTime, productTypeCount,
                                        orderTotal, orderStatus));
                            }
                            mSecondBeanList.putAll(secondList);
                            mFirstBeanList.addAll(firstList);

                            if (length == 0) {
                                ToastUtil.showToast(getActivity(), "暂无数据");
                            } else {
                                mAdapter.notifyDataSetChanged();
                                mListView.setSelection(mFirstBeanList.size() - firstList.size());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtil.showToast(getActivity(), "error:" + throwable.getMessage());
                        Log.e("ndb", "error:" + throwable.getMessage());
                    }
                });
    }
}
