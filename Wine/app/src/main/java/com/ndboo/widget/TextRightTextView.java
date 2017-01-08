package com.ndboo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ndboo.utils.SizeUtil;
import com.ndboo.wine.R;


/**
 * Created by ZhangHang on 2016/8/18.
 * 订单详情上面部分自定义ItemView
 */
public class TextRightTextView extends RelativeLayout {
    private TextView mLabelTextView, mDataTextView;
    //标签和内容的颜色
    private int mLabelTextColor, mDataTextColor;
    //标签和内容的大小
    private float mLabelTextSize, mDataTextSize;
    //标签和内容文字
    private String mLabelText, mDataText;

    public TextRightTextView(Context context) {
        this(context, null);
    }

    public TextRightTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextRightTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextRightTextView);
        mLabelText = typedArray.getString(R.styleable.TextRightTextView_labelText);
        mDataText = typedArray.getString(R.styleable.TextRightTextView_dataText);

        int defaultColor = Color.parseColor("#666666");
        mLabelTextColor = typedArray.getColor(R.styleable.TextRightTextView_labelTextColor, defaultColor);
        mDataTextColor = typedArray.getColor(R.styleable.TextRightTextView_dataTextColor, defaultColor);

        int defaultSize = (int) SizeUtil.sp2px(15,context);
        mLabelTextSize = typedArray.getDimension(R.styleable.TextRightTextView_labelTextSize, defaultSize);
        mDataTextSize = typedArray.getDimension(R.styleable.TextRightTextView_dataTextSize, defaultSize);
        typedArray.recycle();

        LayoutParams params1 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.addRule(CENTER_VERTICAL);
        mLabelTextView = new TextView(context);
        mLabelTextView.setLayoutParams(params1);
        mLabelTextView.setText(mLabelText);
        mLabelTextView.setTextColor(mLabelTextColor);
        mLabelTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLabelTextSize);
        mLabelTextView.setBackgroundColor(Color.TRANSPARENT);
        addView(mLabelTextView);

        LayoutParams params2 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params2.addRule(CENTER_VERTICAL);
        params2.addRule(ALIGN_PARENT_RIGHT);
        mDataTextView = new TextView(context);
        mDataTextView.setLayoutParams(params2);
        mDataTextView.setText(mDataText);
        mDataTextView.setTextColor(mDataTextColor);
        mDataTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mDataTextSize);
        mDataTextView.setBackgroundColor(Color.TRANSPARENT);
        addView(mDataTextView);
    }


    public String getLabelText() {
        return mLabelText;
    }

    public void setLabelText(String labelText) {
        mLabelText = labelText;
        mLabelTextView.setText(mLabelText);
    }

    public String getDataText() {
        return mDataText;
    }

    public void setDataText(String dataText) {
        mDataText = dataText;
        mDataTextView.setText(mDataText);
    }
}
