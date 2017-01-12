package com.ndboo.wine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.ndboo.base.BaseActivity;
import com.ndboo.ui.fragment.OrderFragment;
import com.ndboo.widget.TopBar;
import com.ndboo.widget.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 订单列表
 */
public class OrderListActivity extends BaseActivity {
    //头部
    @BindView(R.id.orderlist_topbar)
    TopBar mTopBar;
    //ViewPager指示器
    @BindView(R.id.orderlist_indicator)
    ViewPagerIndicator mPagerIndicator;
    //ViewPager
    @BindView(R.id.orderlist_viewpager)
    ViewPager mViewPager;

    //订单的状态
    private String[] mStatusArray = {"待付款", "已付款", "派送中", "已完成"};

    //ViewPager中的Fragment
    List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_list;
    }

    @Override
    public void init() {
        initData();
    }

    private void initData() {
        for (int i = 0; i < mStatusArray.length; i++) {
            OrderFragment fragment = newInstance(i);
            mFragmentList.add(fragment);
        }
        initView();
    }

    private void initView() {
        mTopBar.setOnTopBarClickListener(new TopBar.OnTopBarClickListener() {
            @Override
            public void onBackClicked() {
                finish();
            }
        });
        mViewPager.setOffscreenPageLimit(mStatusArray.length - 1);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });

        mPagerIndicator.setTabItemTitles(mStatusArray);
        mPagerIndicator.setViewPager(mViewPager, 0);
    }

    public static OrderFragment newInstance(int type) {
        OrderFragment frag = new OrderFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        frag.setArguments(args);
        return frag;
    }
}
