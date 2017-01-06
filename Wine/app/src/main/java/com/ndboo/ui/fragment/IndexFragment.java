package com.ndboo.ui.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.ndboo.base.BaseFragment;
import com.ndboo.wine.MainActivity;
import com.ndboo.wine.R;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Li on 2016/12/23.
 * 首页
 */

public class IndexFragment extends BaseFragment {

    @BindView(R.id.roll_view_pager)
    RollPagerView mRollPagerView;

    private getWinTypeId mGetWinTypeId;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    public void showContent() {
        super.showContent();
        getCarousel();
    }

    /**
     * 获取轮播
     */
    public void getCarousel() {
        List<String> colors = Arrays.asList(getResources().getStringArray(R.array.roll_view_pager));
        showCarousel(colors);
    }

    /**
     * 显示轮播
     *
     * @param colors 轮播集合
     */
    public void showCarousel(final List<String> colors) {

        mRollPagerView.setPlayDelay(4000);
        mRollPagerView.setAnimationDurtion(500);
        mRollPagerView.setAdapter(new LoopPagerAdapter(mRollPagerView) {
            @Override
            public View getView(ViewGroup container, int position) {
                View view = new View(container.getContext());
                view.setBackgroundColor(Color.parseColor(colors.get(position)));
                return view;
            }


            @Override
            public int getRealCount() {
                return colors.size();

            }
        });


    }



    public void setGetWinTypeId(getWinTypeId getWinTypeId) {
        mGetWinTypeId = getWinTypeId;
    }


    @OnClick({R.id.iv_red_wine, R.id.iv_rice_wine, R.id.iv_liquor, R.id.iv_beer})
    public void onClick(View view) {
        int position= Integer.parseInt(view.getTag().toString());
        ((MainActivity) getActivity()).turnToMall();
        if (mGetWinTypeId != null) {
            mGetWinTypeId.showById(position);

        }
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
