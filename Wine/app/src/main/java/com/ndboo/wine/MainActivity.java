package com.ndboo.wine;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.ndboo.base.BaseActivity;
import com.ndboo.ui.fragment.IndexFragment;
import com.ndboo.ui.fragment.MineFragment;
import com.ndboo.ui.fragment.ShoppingCarFragment;
import com.ndboo.ui.fragment.TypeFragment;
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


    private List<Fragment> mFragments;
    private TypeFragment mTypeFragment;

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
        mTypeFragment = new TypeFragment();

        ShoppingCarFragment carFragment=new ShoppingCarFragment();
        MineFragment mineFragment=new MineFragment();

        mFragments.add(mIndexFragment);
        mFragments.add(mTypeFragment);
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

//        mIndexFragment.setGetWinTypeId(new IndexFragment.getWinTypeId() {
//            @Override
//            public void showById(int id) {
//                mTypeFragment.setCurrentPosition(id);
//            }
//        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            int tag = Integer.parseInt(buttonView.getTag().toString());
            mViewPagerMain.setCurrentItem(tag, false);
        }


    }

    @Override
    public void onBackPressed() {
        if (mTypeFragment.getDropLayout().isMenuOpen()) {
            mTypeFragment.getDropLayout().closeMenu();
        }else {
            super.onBackPressed();
        }

    }


    public void turnToType(){
        mRbType.setChecked(true);
//        Bundle bundle=new Bundle();
//        bundle.putBoolean("interrupt",true);
//        mTypeFragment.setArguments(bundle);
    }
}
