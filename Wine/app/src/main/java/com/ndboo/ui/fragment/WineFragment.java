package com.ndboo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ndboo.adapter.WineAdapter;
import com.ndboo.base.BaseFragment;
import com.ndboo.bean.WineBean;
import com.ndboo.net.RetrofitHelper;
import com.ndboo.utils.SharedPreferencesUtil;
import com.ndboo.wine.MainActivity;
import com.ndboo.wine.R;
import com.ndboo.wine.WineDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Li on 2017/1/7.
 * 商城四种酒类Fragment
 */

public class WineFragment extends BaseFragment {
    private static final String IS_FIRST_FRAGMENT = "isFirstFragment";
    private static final String WINE_TYPE = "wineType";
    @BindView(R.id.refresh_listview)
    ListView mRefreshListView;
    @BindView(R.id.refresh_listview_frame)
    PtrClassicFrameLayout mRefreshListViewFrame;


    private WineAdapter mWineAdapter;
    private List<WineBean> mWineBeen;

    public static WineFragment newInstance(boolean isFirstFragment, String wineType) {

        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_FIRST_FRAGMENT, isFirstFragment);
        bundle.putString(WINE_TYPE, wineType);
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
        mWineBeen = new ArrayList<>();
        mWineAdapter = new WineAdapter(getContext(), this);
        mRefreshListView.setAdapter(mWineAdapter);
        mRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WineDetailActivity.class);
                intent.putExtra("wineId", mWineBeen.get(position).getProductId());
                startActivityForResult(intent, 1);
            }
        });
        boolean isFirst = getArguments().getBoolean(IS_FIRST_FRAGMENT);
        String wineType = getArguments().getString(WINE_TYPE);

//        if (isFirst) {
//            showWinesByType(wineType, SharedPreferencesUtil.getUserId(getContext()));
//
//        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {

            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.turnToShoppingCar();
        }
        if (resultCode==3) {
            String wineType = getArguments().getString(WINE_TYPE);
            showWinesByType(wineType, SharedPreferencesUtil.getUserId(getContext()));
        }
    }

    @Override
    public void firstVisibleDeal() {
        super.firstVisibleDeal();

    }

    @Override
    protected void visibleDeal() {
        super.visibleDeal();
        boolean isFirst = getArguments().getBoolean(IS_FIRST_FRAGMENT);
        String wineType = getArguments().getString(WINE_TYPE);
        showWinesByType(wineType, SharedPreferencesUtil.getUserId(getContext()));

//        if (!isFirst) {
//
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            String wineType = getArguments().getString(WINE_TYPE);
            showWinesByType(wineType, SharedPreferencesUtil.getUserId(getContext()));
        }

    }

    private void showWinesByType(String wineType, String userId) {
        unSubscribe();
        Subscription subscription = RetrofitHelper.getApi()
                .showWinesByType(wineType, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<WineBean>>() {
                    @Override
                    public void call(List<WineBean> wineBeen) {
                        mWineBeen = wineBeen;
                        mWineAdapter.setWines(wineBeen);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
        addSubscription(subscription);
    }

    @Override
    protected void inVisibleDeal() {
        super.inVisibleDeal();
        unSubscribe();
    }
}
