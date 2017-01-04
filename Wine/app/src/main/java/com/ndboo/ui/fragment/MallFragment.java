package com.ndboo.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.ndboo.adapter.ProductAdapter;
import com.ndboo.base.BaseFragment;
import com.ndboo.bean.Type;
import com.ndboo.interfaces.OnItemClickListener;
import com.ndboo.widget.DropLayout;
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

public class MallFragment extends BaseFragment {


    @BindView(R.id.recycler_stage_three)
    RecyclerView mRecyclerStageThree;
    @BindView(R.id.tab_layout_type)
    TabLayout mTabLayoutType;
    @BindView(R.id.drop_filter)
    DropLayout mDropLayout;
    @BindView(R.id.grid_view_filter)
    GridView mGridViewFilter;


    /**
     * 商品筛选条目
     */
    private List<String> mFilters;
    private List<String> wineFlavor;
    private List<String> wineBrand;
    private List<String> wineOrigin;
    private List<String> winePrice;
    private List<List<String>> mLists;
    private List<String> currentList;
    private List<Type> mTypes;

    private List<String> mWineTypes;
    private ProductAdapter mProductAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mall;
    }


    @Override
    public void firstVisibleDeal() {

        initData();
        showProducts();
        showDrop();
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.getIndexFragment().setGetWinTypeId(new IndexFragment.getWinTypeId() {
            @Override
            public void showById(int id) {
                setCurrentPosition(id);
            }
        });
    }

    private void initData() {
        mRecyclerStageThree.setLayoutManager(new LinearLayoutManager(getContext()));
        mFilters = Arrays.asList(getResources().getStringArray(R.array.wine_filter_condition));
        wineFlavor = Arrays.asList(getResources().getStringArray(R.array.wine_flavor));
        wineBrand = Arrays.asList(getResources().getStringArray(R.array.wine_brand));
        wineOrigin = Arrays.asList(getResources().getStringArray(R.array.wine_origin));
        winePrice = Arrays.asList(getResources().getStringArray(R.array.wine_price));
        mLists = new ArrayList<>();
        mLists.add(wineFlavor);
        mLists.add(wineBrand);
        mLists.add(wineOrigin);
        mLists.add(winePrice);
        mWineTypes = Arrays.asList(getResources().getStringArray(R.array.wine_type));
        mTypes = new ArrayList<>();
        for (String wineType : mWineTypes) {
            Type type = new Type(wineType, R.mipmap.ic_wine);
            mTypes.add(type);
            mTabLayoutType.addTab(mTabLayoutType.newTab().setText(wineType));
        }
        mTabLayoutType.setSelectedTabIndicatorHeight(0);
        mTabLayoutType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                resetAllTab();
                tab.setIcon(R.drawable.wine_1_nm);
                tab.setText("");
                Log.e("tag", "selected" + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                Log.e("tag", "unselected" + tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                Log.e("tag", "reselected" + tab.getPosition());
            }
        });
    }

    void resetAllTab() {
        for (int i = 0; i < mTabLayoutType.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayoutType.getTabAt(i);
            tab.setText(mWineTypes.get(i));
            tab.setIcon(null);
        }
    }

    /**
     * 显示商品
     */
    private void showProducts() {
        if (mProductAdapter == null) {
            mProductAdapter = new ProductAdapter(mTypes, getContext());
        }
        mRecyclerStageThree.setAdapter(mProductAdapter);
        mProductAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Toast.makeText(getActivity(), mTypes.get(position).getDescription(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 显示下拉筛选
     */
    private void showDrop() {
        mDropLayout.loadMenu(mFilters);
        mDropLayout.setOnFilterChangeListener(new DropLayout.OnFilterChangeListener() {
            @Override
            public void filterChange(int position) {
                currentList = mLists.get(position);
                mGridViewFilter.setAdapter(new ArrayAdapter<>(getContext(), R.layout.item_filter,
                        mLists.get(position)));
            }
        });
        mGridViewFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), currentList.get(position), Toast.LENGTH_SHORT).show();
                mDropLayout.closeMenu();
            }
        });
    }

    /**
     * 当从首页跳转至分类页时，可以动态刷新所选类别数量
     * RxJava+Retrofit为防止数据出现显示混乱，在每选择时就要取消上一次的订阅
     *
     * @param currentPosition 当前类别编号
     */
    public void setCurrentPosition(int currentPosition) {
        unSubscribe();
        mTabLayoutType.getTabAt(currentPosition).select();
    }

    @Override
    protected void inVisibleDeal() {
        if (mDropLayout != null && mDropLayout.isMenuOpen()) {
            mDropLayout.closeMenu();

        }
    }

    /**
     * 返回筛选布局用来判断退出时，筛选框状态
     *
     * @return mDropLayout
     */
    public DropLayout getDropLayout() {
        return mDropLayout;
    }
}
