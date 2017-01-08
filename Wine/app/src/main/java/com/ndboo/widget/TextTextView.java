package com.ndboo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ndboo.utils.SizeUtil;
import com.ndboo.wine.R;


/**
 * Created by ZhangHang on 2016/8/22.
 */
public class TextTextView extends LinearLayout {
    private TextView mLabelTextView, mDataTextView;
    //文字
    private String mLabelText, mDataText;
    //大小
    private int mLabelTextSize, mDataTextSize;
    //颜色
    private int mLabelTextColor, mDataTextColor;
    //宽度
    private int mLabelTextWidth;

    public TextTextView(Context context) {
        this(context, null);
    }

    public TextTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //默认对齐宽度
        int defaultLabelWidth = (int) SizeUtil.dp2px(90,context);
        //默认文字大小
        int defaultLabelSize = (int) SizeUtil.sp2px(15,context);
        int defaultDataSize = (int) SizeUtil.sp2px(16,context);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextTextView);
        mLabelTextWidth = (int) typedArray.getDimension(R.styleable.TextTextView_leftTextWidth, defaultLabelWidth);

        mLabelText = typedArray.getString(R.styleable.TextTextView_leftText);
        mLabelTextColor = typedArray.getColor(R.styleable.TextTextView_leftTextColor, Color.parseColor("#666666"));
        mLabelTextSize = (int) typedArray.getDimension(R.styleable.TextTextView_leftTextSize, defaultLabelSize);

        mDataText = typedArray.getString(R.styleable.TextTextView_rightText);
        mDataTextColor = typedArray.getColor(R.styleable.TextTextView_rightTextColor, Color.BLACK);
        mDataTextSize = (int) typedArray.getDimension(R.styleable.TextTextView_rightTextSize, defaultDataSize);
        typedArray.recycle();

        this.setOrientation(HORIZONTAL);

        LayoutParams params2 = new LayoutParams(mLabelTextWidth, LayoutParams.WRAP_CONTENT);
        mLabelTextView = new TextView(context);
        mLabelTextView.setLayoutParams(params2);
        mLabelTextView.setText(mLabelText);
        mLabelTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLabelTextSize);
        mLabelTextView.setTextColor(mLabelTextColor);
        mLabelTextView.setBackgroundColor(Color.TRANSPARENT);
        addView(mLabelTextView);

        LayoutParams params3 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        mDataTextView = new TextView(context);
        mDataTextView.setLayoutParams(params3);
        mDataTextView.setText(mDataText);
        mDataTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mDataTextSize);
        mDataTextView.setTextColor(mDataTextColor);
        mDataTextView.setBackgroundColor(Color.TRANSPARENT);
        addView(mDataTextView);
    }

    public void setDataText(String dataText) {
        mDataText = dataText;
        mDataTextView.setText(mDataText);
    }

    public String getDataText() {
        return mDataText;
    }
}
