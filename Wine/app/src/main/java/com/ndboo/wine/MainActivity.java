package com.ndboo.wine;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.ndboo.base.BaseActivity;
import com.ndboo.ui.fragment.BusinessFragment;
import com.ndboo.ui.fragment.IndexFragment;
import com.ndboo.ui.fragment.MallFragment;
import com.ndboo.ui.fragment.MineFragment;
import com.ndboo.widget.FixedViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {


    @BindView(R.id.view_pager_main)
    FixedViewPager mViewPagerMain;
    @BindView(R.id.rb_index)
    RadioButton mRbIndex;
    @BindView(R.id.rb_type)
    RadioButton mRbType;
    @BindView(R.id.rb_shopping_car)
    RadioButton mRbShoppingCar;
    @BindView(R.id.rb_mine)
    RadioButton mRbMine;

    /**
     * fragment集合
     * 包含首页、商城、商家、我的四个界面
     */
    private List<Fragment> mFragments;
    private MallFragment mMallFragment;

    public IndexFragment getIndexFragment() {
        return mIndexFragment;
    }

    private IndexFragment mIndexFragment;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        mFragments = new ArrayList<>();

        mIndexFragment = new IndexFragment();
        mMallFragment = new MallFragment();
        BusinessFragment carFragment=new BusinessFragment();
        MineFragment mineFragment=new MineFragment();
        mFragments.add(mIndexFragment);
        mFragments.add(mMallFragment);
        mFragments.add(carFragment);
        mFragments.add(mineFragment);

        mViewPagerMain.setOffscreenPageLimit(mFragments.size());
        mViewPagerMain.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        });
        mRbIndex.setOnCheckedChangeListener(this);
        mRbType.setOnCheckedChangeListener(this);
        mRbShoppingCar.setOnCheckedChangeListener(this);
        mRbMine.setOnCheckedChangeListener(this);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            int tag = Integer.parseInt(buttonView.getTag().toString());
            mViewPagerMain.setCurrentItem(tag, false);
        }
    }

    /**
     * 返回键处理
     */
    @Override
    public void onBackPressed() {
        if (mMallFragment.getDropLayout().isMenuOpen()) {
            mMallFragment.getDropLayout().closeMenu();
        }else {
            super.onBackPressed();
        }
    }

    /**
     * 转到商城
     */
    public void turnToMall(){
        mRbType.setChecked(true);
    }
}
