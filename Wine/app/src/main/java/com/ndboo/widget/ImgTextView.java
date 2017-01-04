package com.ndboo.widget;

import android.content.Context;
import android.content.res.TypedArray;
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
 * Created by Administrator on 2016/12/27.
 */

public class ImgTextView extends LinearLayout {
    private ImageView mLeftImage;
    private ImageView mRightImage;
    private TextView mTextContent;
    private int mLeftImgRes;
    private String mText;
    private int mRightImgRes;
    private int mTextColor;
    private int mTextSize;
    private int mLeftPadding;

    public ImgTextView(Context context) {
        super(context);
    }

    public ImgTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ImgTextView);
        mText = array.getString(R.styleable.ImgTextView_itv_text_content);
        mLeftImgRes = array.getResourceId(R.styleable.ImgTextView_itv_left_image, 0);
        mRightImgRes = array.getResourceId(R.styleable.ImgTextView_itv_right_image, 0);
        mTextColor = array.getColor(R.styleable.ImgTextView_itv_text_color, 0x000000);
        mTextSize = array.getDimensionPixelSize(R.styleable.ImgTextView_itv_text_size,
                (int) SizeUtil.sp2px(12,getContext()));
        mLeftPadding=array.getDimensionPixelSize(R.styleable.ImgTextView_itv_left_padding,
                (int) SizeUtil.dp2px(12,getContext()));
        array.recycle();
        init();
    }

    private void init() {
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(LinearLayout.HORIZONTAL);

        mLeftImage = new ImageView(getContext());
        mTextContent = new TextView(getContext());
        mRightImage = new ImageView(getContext());

        mLeftImage.setImageResource(mLeftImgRes);
        mRightImage.setImageResource(mRightImgRes);
        mTextContent.setText(mText);

        mTextContent.setPadding(mLeftPadding, 0, 0, 0);
        LinearLayout.LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        params.gravity = Gravity.CENTER_VERTICAL;
        mTextContent.setLayoutParams(params);
        mTextContent.setTextColor(mTextColor);
        mTextContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);

        addView(mLeftImage);
        addView(mTextContent);
        addView(mRightImage);

    }

}
