package com.ndboo.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.rollviewpager.RollPagerView;
import com.ndboo.adapter.CarouselAdapter;
import com.ndboo.base.BaseFragment;
import com.ndboo.bean.CarouselBean;
import com.ndboo.net.RetrofitHelper;
import com.ndboo.wine.MainActivity;
import com.ndboo.wine.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Li on 2016/12/23.
 * 首页
 */

public class IndexFragment extends BaseFragment {

    @BindView(R.id.roll_view_pager)
    RollPagerView mRollPagerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    private getWinTypeId mGetWinTypeId;
    private CarouselAdapter mCarouselAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    public void showContent() {
        super.showContent();
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showCarousel();
            }
        });
        showCarousel();
    }

    /**
     * 显示轮播
     */
    public void showCarousel() {
        mRollPagerView.setPlayDelay(4000);
        mRollPagerView.setAnimationDurtion(500);
        mCarouselAdapter = new CarouselAdapter(mRollPagerView, getActivity());
        mRollPagerView.setAdapter(mCarouselAdapter);
        Subscription subscription = RetrofitHelper.getApi()
                .getCarousel()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<CarouselBean>>() {
                    @Override
                    public void call(List<CarouselBean> carouselBeen) {
                        mSwipeRefresh.setRefreshing(false);
                        mCarouselAdapter.setCarouselBeanList(carouselBeen);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mSwipeRefresh.setRefreshing(false);
                    }
                });
        addSubscription(subscription);
    }


    public void setGetWinTypeId(getWinTypeId getWinTypeId) {
        mGetWinTypeId = getWinTypeId;
    }


    @OnClick({R.id.iv_red_wine, R.id.iv_rice_wine, R.id.iv_liquor, R.id.iv_beer})
    public void onClick(View view) {
        int position = Integer.parseInt(view.getTag().toString());
        ((MainActivity) getActivity()).turnToMall();
        if (mGetWinTypeId != null) {
            mGetWinTypeId.showById(position);

        }
    }


    @OnClick(R.id.iv_index_phone)
    public void onClick() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + "051266155111"));
        startActivity(intent);
    }



    public interface getWinTypeId {
        void showById(int id);
    }

    class TypeHolder {

        @BindView(R.id.image)
        ImageView mImageView;
        @BindView(R.id.text)
        TextView mTextView;

        TypeHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
