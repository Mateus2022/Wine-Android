package com.ndboo.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ndboo.adapter.OrderAdapter;
import com.ndboo.wine.R;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * Created by ZhangHang on 2016/9/21.
 */

public class OrderFragment extends Fragment {
    private static final int WHAT_MY_ORDER = 1;

    //每页显示的数量
    private static final int NUM_PER_PAGE = 5;

    //0表示加载，1表示刷新
    private static final int TYPE_LODING = 0;
    private static final int TYPE_REFRESH = 1;

    private Activity mActivity;

    //当前Fragment是哪个分类
    private int mCurrentSort;

    //当前显示的是第几页
    private int mCurrentPage = 1;

    //列表
    private PtrClassicFrameLayout mPtrClassicFrameLayout;
    private ListView mListView;
    private OrderAdapter mAdapter;
    private List<String> mStringList;

    //标记当前是刷新还是加载,默认刷新
    private int mLoadingType = TYPE_REFRESH;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = getActivity();
        mCurrentSort = getArguments().getInt("type");
        View view = inflater.inflate(R.layout.fragment_order, null);

        initData();
        initView(view);
        return mPtrClassicFrameLayout;
    }

    private void initData() {
        mStringList = new ArrayList<>();
        mStringList.add("11111111111");
        mStringList.add("2222222222222");
        mStringList.add("333333333333333");
        mStringList.add("444444444444444");
        mStringList.add("55555555");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getData();
        }
    }

    private void initView(View view) {

        mPtrClassicFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.order_frame);
        mListView = (ListView) view.findViewById(R.id.order_listview);
        mAdapter = new OrderAdapter(mActivity, mStringList);
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

        addListener();
    }

    private void addListener() {
        mPtrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                //加载
                mLoadingType = TYPE_LODING;
                //页数增加
                mCurrentPage++;
                //获取数据
                getData();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //刷新
                mLoadingType = TYPE_REFRESH;
                //重置为第一页
                mCurrentPage = 1;
                //获取数据
                getData();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    /**
     * 获取数据
     */
    private void getData() {

    }
}
