package com.ndboo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ndboo.wine.R;

import java.util.List;

/**
 * Created by Li on 2016/12/28.
 * 筛选菜单
 */

public class DropLayout extends LinearLayout implements View.OnClickListener {
    private List<String> mTitles;
    private LinearLayout mLayoutFilterBar;
    private FrameLayout mLayoutFilterContent;
    private Drawable mFilterDrawable;
    private Drawable mUnFilterDrawable;
    private int currentTabPosition = -1;
    private int mSelectedTextColor;
    private int mUnSelectedTextColor;
    private int mTextSize = (int) sp2px(12);
    private int mDividerColor;
    private int mDividerMargin = (int) dp2px(5);
    private int mSelectedImg = android.R.drawable.arrow_up_float;
    private int mUnSelectedImg = android.R.drawable.arrow_down_float;

    private boolean isMenuOpen = false;
    private OnFilterChangeListener mOnFilterChangeListener;

    public DropLayout(Context context) {
        this(context, null);
    }

    public DropLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);

    }

    void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DropLayout);
        mSelectedTextColor = array.getColor(R.styleable.DropLayout_selectedTextColor, Color.parseColor("#0000ff"));
        mUnSelectedTextColor = array.getColor(R.styleable.DropLayout_unSelectedTextColor, Color.parseColor("#000000"));
        mTextSize = array.getDimensionPixelSize(R.styleable.DropLayout_textSize, mTextSize);
        mDividerColor = array.getColor(R.styleable.DropLayout_dividerColor, Color.parseColor("#0000ff"));
        mDividerMargin = array.getDimensionPixelOffset(R.styleable.DropLayout_dividerMargin, mDividerMargin);
        mSelectedImg = array.getResourceId(R.styleable.DropLayout_selectedImg, mSelectedImg);
        mUnSelectedImg = array.getResourceId(R.styleable.DropLayout_unSelectedImg, mUnSelectedImg);
        array.recycle();
    }

    public void loadMenu(List<String> titles) {
        mTitles = titles;
        init();
    }

    private void init() {


        setOrientation(LinearLayout.VERTICAL);

        mLayoutFilterBar = (LinearLayout) getChildAt(0);

        FrameLayout layout = (FrameLayout) getChildAt(2);
        mLayoutFilterContent = (FrameLayout) layout.getChildAt(1);

        mLayoutFilterContent.setVisibility(GONE);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1f);

        mFilterDrawable = getResources().getDrawable(mSelectedImg);
        mUnFilterDrawable = getResources().getDrawable(mUnSelectedImg);

        LayoutParams dividerParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        dividerParams.width = 1;
        dividerParams.setMargins(0, mDividerMargin, 0, mDividerMargin);

        for (int i = 0; i < mTitles.size(); i++) {

            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_filter_condition, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_filter);
            ImageView imageView = (ImageView) view.findViewById(R.id.img_filter);
            imageView.setImageDrawable(mUnFilterDrawable);

            textView.setText(mTitles.get(i));
            textView.setTextColor(mUnSelectedTextColor);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            view.setLayoutParams(params);
            mLayoutFilterBar.addView(view);

            View dividerView = new View(getContext());
            dividerView.setLayoutParams(dividerParams);
            dividerView.setBackgroundColor(mDividerColor);

            if (i < mTitles.size() - 1) {
                mLayoutFilterBar.addView(dividerView);
            }
            view.setTag(i);
            view.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        int tag = Integer.parseInt(v.getTag().toString());
        //当前位置就是点击位置，则取消
        if (currentTabPosition == tag) {
            closeMenu();
            resetAllTab();
        }
        //切换
        else {
            currentTabPosition = tag;
            switchTab(tag);
        }
    }

    private void switchTab(int tag) {
        resetAllTab();
        showTab(tag * 2);
        mOnFilterChangeListener.filterChange(tag);

    }

    private void resetAllTab() {
        for (int i = 0; i < mLayoutFilterBar.getChildCount(); i += 2) {
            LinearLayout layout = (LinearLayout) mLayoutFilterBar.getChildAt(i);
            TextView textView = (TextView) layout.findViewById(R.id.tv_filter);
            ImageView imageView = (ImageView) layout.findViewById(R.id.img_filter);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            textView.setTextColor(mUnSelectedTextColor);
            imageView.setImageDrawable(mUnFilterDrawable);
        }
    }

    private void showTab(int tag) {

        isMenuOpen = true;
        LinearLayout layout = (LinearLayout) mLayoutFilterBar.getChildAt(tag);
        TextView textView = (TextView) layout.findViewById(R.id.tv_filter);
        ImageView imageView = (ImageView) layout.findViewById(R.id.img_filter);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        textView.setTextColor(mUnSelectedTextColor);
        imageView.setImageDrawable(mFilterDrawable);


        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        textView.setTextColor(mSelectedTextColor);

        if (mLayoutFilterContent.getVisibility() == GONE) {
            mLayoutFilterContent.setVisibility(VISIBLE);
        }
        mLayoutFilterContent.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.translate_filter_in));
//        for (int i = 0; i < mLayoutFilterContent.getChildCount(); i++) {
//            if (i==tag/2) {
//                mLayoutFilterContent.getChildAt(i).setVisibility(VISIBLE);
//            }else {
//                mLayoutFilterContent.getChildAt(i).setVisibility(GONE);
//            }
//
//        }


    }

    public void setOnFilterChangeListener(OnFilterChangeListener onFilterChangeListener) {
        mOnFilterChangeListener = onFilterChangeListener;
    }

    public void closeMenu() {
        isMenuOpen = false;
        mLayoutFilterContent.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.translate_filter_out));

        mLayoutFilterContent.setVisibility(GONE);
        resetAllTab();
        currentTabPosition = -1;
    }

    private float sp2px(float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, getResources().getDisplayMetrics());
    }

    private float dp2px(float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }

    public boolean isMenuOpen() {
        return isMenuOpen;
    }

    public interface OnFilterChangeListener {
        void filterChange(int position);
    }
}
