package com.ndboo.wine;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.jauker.widget.BadgeView;
import com.ndboo.base.BaseActivity;
import com.ndboo.net.RetrofitHelper;
import com.ndboo.ui.fragment.IndexFragment;
import com.ndboo.ui.fragment.MallFragment;
import com.ndboo.ui.fragment.MineFragment;
import com.ndboo.ui.fragment.ShoppingCarFragment;
import com.ndboo.utils.SharedPreferencesUtil;
import com.ndboo.utils.SizeUtil;
import com.ndboo.widget.FixedViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
    @BindView(R.id.car_flow)
    Button mCarFlow;
    /**
     * fragment集合
     * 包含首页、商城、商家、我的四个界面
     */
    private List<Fragment> mFragments;
    private MallFragment mMallFragment;
    private IndexFragment mIndexFragment;
    private MineFragment mMineFragment;
    private ShoppingCarFragment mShoppingCarFragment;
    private long exitTime = 0;
    private String mProductCount;
    private BadgeView mBadgeView;

    public IndexFragment getIndexFragment() {
        return mIndexFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
//        checkLocationPermission();
        mFragments = new ArrayList<>();

        mIndexFragment = new IndexFragment();
        mMallFragment = new MallFragment();
        mShoppingCarFragment = new ShoppingCarFragment();
        mMineFragment = new MineFragment();
        mFragments.add(mIndexFragment);
        mFragments.add(mMallFragment);
        mFragments.add(mShoppingCarFragment);
        mFragments.add(mMineFragment);

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
        mBadgeView = new BadgeView(this);
        int simpleWidth = getResources().getDisplayMetrics().widthPixels / 4;
        mBadgeView.setTargetView(mCarFlow);
        mBadgeView.setBadgeMargin(0, 3, (int) SizeUtil.px2dp(simpleWidth * 1.0f * 0.1f, getApplicationContext()), 0);
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
     * 此处暂时用来处理按下时，在商城Tab中，下拉筛选是否关闭
     */
    @Override
    public void onBackPressed() {
        int currentPage = mViewPagerMain.getCurrentItem();
        if (currentPage == 3) {
            if (mMineFragment.isShow()) {
                mMineFragment.closePop();
            } else {
                exit();
            }
        } else if (currentPage == 2) {
            if (mShoppingCarFragment.isShow()) {
                mShoppingCarFragment.closePop();
            } else {
                exit();
            }
        }
//        else if (currentPage == 1) {
//            if (mMallFragment.getDropLayout().isMenuOpen()) {
//                mMallFragment.getDropLayout().closeMenu();
//            } else {
//                super.onBackPressed();
//            }
//        }
        else {
            exit();
        }
    }

    private void exit() {
        if (System.currentTimeMillis() - exitTime > 2000) {

            if (!mRbIndex.isChecked()) {
                mRbIndex.setChecked(true);
            }
            exitTime = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
        } else {
            finish();
            System.exit(0);
        }
    }

    /**
     * 转到商城
     */
    public void turnToMall() {
        mRbType.setChecked(true);
    }

    public void turnToShoppingCar() {
        mRbShoppingCar.setChecked(true);
    }

    public void queryWineNum() {
        Subscription subscription = RetrofitHelper.getApi()
                .queryCarNum(SharedPreferencesUtil.getUserId(getApplicationContext()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            mProductCount = object.getString("productCount");
                            if (mBadgeView != null) {
                                mBadgeView.setText(mProductCount);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });

        addSubscription(subscription);
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryWineNum();
    }
}
