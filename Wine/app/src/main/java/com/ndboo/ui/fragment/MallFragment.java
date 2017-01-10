package com.ndboo.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.ndboo.base.BaseFragment;
import com.ndboo.wine.MainActivity;
import com.ndboo.wine.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Li on 2016/12/26.
 * “商城”界面
 */

public class MallFragment extends BaseFragment{


    @BindView(R.id.tab_layout_type)
    TabLayout mTabLayoutType;
    @BindView(R.id.view_pager_wine)
    ViewPager mViewPagerWine;


    private List<Fragment> mFragments;
    private List<String> mMWineTypes;
    private int mCurrentPosition=0;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mall;
    }

    @Override
    public void firstVisibleDeal() {
        initData();
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.getIndexFragment().setGetWinTypeId(new IndexFragment.getWinTypeId() {
            @Override
            public void showById(int id) {
                setCurrentPosition(id);
            }
        });


    }

    @Override
    protected void visibleDeal() {
        super.visibleDeal();
        mViewPagerWine.setAdapter(new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mMWineTypes.get(position);
            }
        });
        mViewPagerWine.setCurrentItem(mCurrentPosition);
    }

    @Override
    protected void inVisibleDeal() {
        super.inVisibleDeal();
        if (mTabLayoutType != null) {
            mCurrentPosition=mTabLayoutType.getSelectedTabPosition();
        }
    }

    private void initData() {

        //酒的种类，目前有四种：红酒、黄酒、白酒、啤酒
        mMWineTypes = Arrays.asList(getResources().getStringArray(R.array.wine_type));
        mFragments=new ArrayList<>();
        mViewPagerWine.setOffscreenPageLimit(mMWineTypes.size());
        mTabLayoutType.setupWithViewPager(mViewPagerWine);
        addOnTabSelectedListener();
        for (int i = 0; i < mMWineTypes.size(); i++) {
            if (i==0) {
                mFragments.add(WineFragment.newInstance(true,i+""));
                continue;
            }
            mFragments.add(WineFragment.newInstance(false,i+""));
        }
        /**
         * 为mTabLayoutType添加addTabSelectedListener监听事件，
         * 要放在为mTabLayoutType添加TabItem之前
         * 这样的话mTabLayoutType可以监听所有发生的变化
         * 不会漏掉添加TabItem时默认位置为0的事件
         */
    }

    /**
     * 监听Tab状态变化
     */
    private void addOnTabSelectedListener() {
        mTabLayoutType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            /**
             * 使用RxJava+Retrofit为防止数据出现显示混乱，在Tab状态发生变化时
             * 必须要取消上一次的订阅；
             * 只需调用父类中的方法unSubscribe即可
             * @param tab   选中的类目
             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                unSubscribe();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /**
     * 代码控制Tab选择，触发Tab变化事件
     *
     * @param currentPosition 当前类别编号
     * @see #addOnTabSelectedListener()
     */
    public void setCurrentPosition(int currentPosition) {
        mTabLayoutType.getTabAt(currentPosition).select();
    }

}
