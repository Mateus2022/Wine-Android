package com.ndboo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ndboo.adapter.WineAdapter;
import com.ndboo.base.BaseFragment;
import com.ndboo.bean.Wine;
import com.ndboo.wine.LoginActivity;
import com.ndboo.wine.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by Li on 2017/1/7.
 * 商城四种酒类Fragment
 */

public class WineFragment extends BaseFragment {
    private static final String IS_FIRST_FRAGMENT = "isFirstFragment";
    @BindView(R.id.refresh_listview)
    ListView mRefreshListView;
    @BindView(R.id.refresh_listview_frame)
    PtrClassicFrameLayout mRefreshListViewFrame;



    private WineAdapter mWineAdapter;
    public static WineFragment newInstance(boolean isFirstFragment) {

        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_FIRST_FRAGMENT, isFirstFragment);
        WineFragment fragment = new WineFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_refresh_listview;
    }

    @Override
    public void showContent() {
        super.showContent();
        boolean isFirst=getArguments().getBoolean(IS_FIRST_FRAGMENT);
        if (isFirst) {
            List<Wine> wines=new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Wine wine=new Wine(R.drawable.ic_type+"","20","30","52°尖庄曲酒450ml*2双瓶礼盒","0");
                wines.add(wine);
            }
            mWineAdapter=new WineAdapter(getContext(),wines);
            mRefreshListView.setAdapter(mWineAdapter);
        }


    }

    @Override
    public void firstVisibleDeal() {
        super.firstVisibleDeal();

    }

    @Override
    protected void visibleDeal() {
        super.visibleDeal();
        boolean isFirst=getArguments().getBoolean(IS_FIRST_FRAGMENT);
        Log.e("tag","visible");
        if (!isFirst) {
            List<Wine> wines=new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Wine wine=new Wine(R.drawable.ic_type+"","20","30","52°尖庄曲酒450ml*2双瓶礼盒","0");
                wines.add(wine);
            }
            mWineAdapter=new WineAdapter(getContext(),wines);
            mRefreshListView.setAdapter(mWineAdapter);
            mRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            });
        }
    }


}
