package com.ndboo.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.ndboo.bean.CarouselBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/12.
 */

public class CarouselAdapter extends LoopPagerAdapter {

    private Activity activity;
    private List<CarouselBean> carouselBeanList;
    public void setCarouselBeanList(List<CarouselBean> carouselBeanList) {
        this.carouselBeanList = carouselBeanList;
notifyDataSetChanged();
    }

    public CarouselAdapter(RollPagerView rollPagerView, Activity activity){
        super(rollPagerView);
        this.activity = activity;
        carouselBeanList = new ArrayList<>();
    }


    @Override
    public View getView(ViewGroup container, final int position) {

        ImageView view = new ImageView(activity);
        String picPath = carouselBeanList.get(position).getPicturePath();
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(activity).load(picPath).into(view);
        return view;
    }

    @Override
    public int getRealCount() {
        return carouselBeanList.size();
    }
}
