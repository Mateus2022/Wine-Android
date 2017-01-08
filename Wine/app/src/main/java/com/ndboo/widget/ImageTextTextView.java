package com.ndboo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ndboo.utils.SizeUtil;
import com.ndboo.wine.R;


/**
 * Created by ZhangHang on 2016/8/19.
 */
public class ImageTextTextView extends LinearLayout {
    private ImageView mImageView;
    private TextView mLabelTextView, mDataTextView;

    //图片
    private Drawable mDrawable;
    //文本
    private String mLabelString, mDataString;


    public ImageTextTextView(Context context) {
        this(context, null);
    }

    public ImageTextTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageTextTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageTextTextView);
        mDrawable = typedArray.getDrawable(R.styleable.ImageTextTextView_detailImage);
        mLabelString = typedArray.getString(R.styleable.ImageTextTextView_detaiLabel);
        mDataString = typedArray.getString(R.styleable.ImageTextTextView_detailData);
        typedArray.recycle();

        this.setOrientation(HORIZONTAL);
        this.setBackgroundColor(Color.WHITE);
        int paddingLeft = (int) SizeUtil.dp2px(13,context);
        int paddingRight = (int) SizeUtil.dp2px(10,context);
        int paddingTop = (int) SizeUtil.dp2px(10,context);
        this.setPadding(paddingLeft, paddingTop, paddingRight, paddingTop);

        mImageView = new ImageView(context);
        LayoutParams params1 = new LayoutParams((int) SizeUtil.dp2px(25,context), LayoutParams.WRAP_CONTENT);
        params1.gravity = Gravity.CENTER;
        mImageView.setLayoutParams(params1);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        mImageView.setImageDrawable(mDrawable);
        addView(mImageView);

        //字体大小，字体颜色
        int textSize = (int) SizeUtil.sp2px(14,context);
        int textColor = Color.BLACK;

        mLabelTextView = new TextView(context);
        LayoutParams params2 = new LayoutParams((int) SizeUtil.dp2px(80,context),
                LayoutParams.WRAP_CONTENT);
        params2.gravity = Gravity.CENTER_VERTICAL;
        int margin = (int) SizeUtil.dp2px(6,context);
        params2.setMargins(margin, 0, 0, 0);
        mLabelTextView.setLayoutParams(params2);
        mLabelTextView.setText("" + mLabelString);
        mLabelTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mLabelTextView.setTextColor(textColor);
        addView(mLabelTextView);

        mDataTextView = new TextView(context);
        LayoutParams params3 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        params3.gravity = Gravity.CENTER_VERTICAL;
        mDataTextView.setLayoutParams(params3);
        mDataTextView.setText("" + mDataString);
        mDataTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mDataTextView.setTextColor(textColor);
        addView(mDataTextView);
    }

    public void setDataString(String dataString) {
        mDataString = dataString;
        mDataTextView.setText(mDataString);
    }
}
