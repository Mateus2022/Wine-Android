package com.ndboo.ui.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.lcodecore.tkrefreshlayout.Footer.LoadingView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
import com.ndboo.adapter.ProductAdapter;
import com.ndboo.adapter.WineTypeAdapter;
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
 * 分类界面
 */

public class TypeFragment extends BaseFragment {

    @BindView(R.id.lvWineType)
    ListView mLvWineType;
    @BindView(R.id.recycler_stage_three)
    RecyclerView mRecyclerStageThree;
    @BindView(R.id.refresh_layout)
    TwinklingRefreshLayout mRefreshLayout;

    @BindView(R.id.drop_filter)
    DropLayout mDropLayout;
    @BindView(R.id.grid_view_filter)
    GridView mGridViewFilter;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mRefreshLayout.finishRefreshing();
                    break;
                case 1:
                    mRefreshLayout.finishLoadmore();
                    break;
            }
        }
    };

    private List<String> mTitles;
    private List<String> wineFlavor;
    private List<String> wineBrand;
    private List<String> wineOrigin;
    private List<String> winePrice;
    private List<List<String>> mLists;
    private List<String> currentList;
    private List<Type> mTypes;

    private List<String> mWineTypes;
    private int mCurrentPosition = 0;
    private WineTypeAdapter mWineTypeAdapter;
    private ProductAdapter mProductAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_type;
    }


    @Override
    public void firstVisibleDeal() {

        initData();
        showWineType();
        showProducts();
        showDrop();
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.getIndexFragment().setGetWinTypeId(new IndexFragment.getWinTypeId() {
            @Override
            public void showById(int id) {
//                showWineType();
                setCurrentPosition(id);
            }
        });
        Log.e("tag", mCurrentPosition + "");

    }

    private void initData() {
        mRecyclerStageThree.setLayoutManager(new LinearLayoutManager(getContext()));
        mTitles = Arrays.asList("香型", "品牌", "产地", "价格");
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
        }
        SinaRefreshView sinaRefreshView = new SinaRefreshView(getContext());
        sinaRefreshView.setArrowResource(R.mipmap.ic_arrow_refresh);
        mRefreshLayout.setHeaderView(sinaRefreshView);
        mRefreshLayout.setBottomView(new LoadingView(getContext()));
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                mHandler.sendEmptyMessageDelayed(0, 3000);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mHandler.sendEmptyMessageDelayed(1, 3000);
            }

        });
    }

    /**
     * 显示商品类别
     */
    private void showWineType() {
        if (mWineTypeAdapter == null) {
            mWineTypeAdapter = new WineTypeAdapter(mWineTypes, mCurrentPosition, getContext());
            mLvWineType.setAdapter(mWineTypeAdapter);
        }
        mLvWineType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mDropLayout.isMenuOpen()) {
                    mDropLayout.closeMenu();
                    return;
                }
                if (mWineTypeAdapter.getCurrentPosition() != position) {
                    mWineTypeAdapter.setCurrentPosition(position);
                    mLvWineType.setSelection(position);
                }
            }
        });
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
        mDropLayout.loadMenu(mTitles);
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
     * @param currentPosition 当前类别编号
     */
    public void setCurrentPosition(int currentPosition) {
        unSubscribe();
        mCurrentPosition = currentPosition;
        mWineTypeAdapter.setCurrentPosition(currentPosition);
        mLvWineType.setSelection(mCurrentPosition);
    }
    @Override
    protected void inVisibleDeal() {
        if (mDropLayout != null&&mDropLayout.isMenuOpen()) {
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
