package com.ndboo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ndboo.utils.SizeUtil;
import com.ndboo.wine.R;

/**
 * Created by ZhangHang on 2016/8/10.
 * 文本加指示器
 */
public class TextIndicatorView extends RelativeLayout {

    private TextView mTextView;
    private View mView;

    //文本文字
    private String mText;

    //指示器的颜色
    private int mIndicatorColor;

    //文本颜色
    private int mTextColor;

    //是否显示指示器
    private boolean mIsShowIndicator;

    //指示器的高度，比文本宽多少(单位px)
    private int mIndicatorHeight, mIndicatorWidth;

    //文本测量、文本宽度
    private TextPaint mTextPaint;
    private float mTextWidth;

    public TextIndicatorView(Context context) {
        this(context, null);
    }

    public TextIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextIndicatorView);

        //分类文字
        mTextView = new TextView(context);
        LayoutParams params1 = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params1.addRule(CENTER_IN_PARENT);
        mTextView.setLayoutParams(params1);
        mText = typedArray.getString(R.styleable.TextIndicatorView_indicatorText);
        mTextView.setText("" + mText);
        mTextView.setBackgroundColor(Color.TRANSPARENT);
        //获取文本颜色
        mTextColor = typedArray.getColor(R.styleable.TextIndicatorView_indicatorTextColor,
                getResources().getColor(R.color.LightGray));
        mTextView.setTextColor(mTextColor);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        this.addView(mTextView);

        //测量textview文字宽度
        mTextPaint = mTextView.getPaint();
        mTextWidth = mTextPaint.measureText((mText == null) ? "" : mText);
        //底部指示器
        mView = new View(context);
        mIndicatorHeight = (int) typedArray.getDimension(R.styleable.TextIndicatorView_indicatorHeight,
                SizeUtil.dp2px(3,context));
        //获取指示器宽
        mIndicatorWidth = (int) typedArray.getDimension(R.styleable.TextIndicatorView_indicatorWidth,
                SizeUtil.dp2px(12,context));
        LayoutParams params2 = new LayoutParams(
                (int) mTextWidth + mIndicatorWidth, mIndicatorHeight);
        params2.addRule(ALIGN_PARENT_BOTTOM);
        mView.setLayoutParams(params2);
        mView.setBackgroundColor(Color.TRANSPARENT);
        //获取指示器颜色
        mIndicatorColor = typedArray.getColor(R.styleable.TextIndicatorView_indicatorColor,
                getResources().getColor(R.color.themeColor));
        mView.setBackgroundColor(mIndicatorColor);
        //获取是否显示指示器
        mIsShowIndicator = typedArray.getBoolean(R.styleable.TextIndicatorView_isShowIndicator, false);
        if (mIsShowIndicator) {
            mView.setVisibility(VISIBLE);
        } else {
            mView.setVisibility(INVISIBLE);
        }
        this.addView(mView);

        typedArray.recycle();
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
        mTextView.setText(text);
        //改变指示器宽度
        mTextWidth = mTextPaint.measureText((mText == null) ? "" : mText);
        LayoutParams params = new LayoutParams(
                (int) mTextWidth + mIndicatorWidth, mIndicatorHeight);
        params.addRule(ALIGN_PARENT_BOTTOM);
        mView.setLayoutParams(params);
    }

    public void setIndicatorColor(int indicatorColor) {
        this.mIndicatorColor = indicatorColor;
        mView.setBackgroundColor(indicatorColor);
    }

    public boolean isShowIndicator() {
        return mIsShowIndicator;
    }

    public void setShowIndicator(boolean showIndicator) {
        mIsShowIndicator = showIndicator;
        if (showIndicator) {
            mView.setVisibility(VISIBLE);
            mTextView.setTextColor(Color.WHITE);
        } else {
            mView.setVisibility(INVISIBLE);
            mTextView.setTextColor(Color.parseColor("#686868"));
        }
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        mTextView.setTextColor(mTextColor);
    }
}
