package com.ndboo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ndboo.wine.R;

/**
 * Created by ZhangHang on 2017/1/4.
 */

public class TopBar extends RelativeLayout {
    private ImageView mBackImageView;
    private TextView mTitleTextView;

    //标题文字
    private String mTitleString;
    //是否显示返回图标
    private boolean mIsShowBackImg;

    private OnTopBarClickListener mOnTopBarClickListener;

    public TopBar(Context context) {
        this(context, null);
    }

    public TopBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        mTitleString = typedArray.getString(R.styleable.TopBar_topbar_title);
        mIsShowBackImg = typedArray.getBoolean(R.styleable.TopBar_topbar_isShowBackImg, true);
        typedArray.recycle();

        LayoutInflater.from(context).inflate(R.layout.layout_topbar, this);
        mBackImageView = (ImageView) findViewById(R.id.topbar_back);
        mTitleTextView = (TextView) findViewById(R.id.topbar_title);

        mTitleTextView.setText((mTitleString == null) ? "" : mTitleString);
        if (mIsShowBackImg) {
            mBackImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnTopBarClickListener != null) {
                        mOnTopBarClickListener.onBackClicked();
                    }
                }
            });
        } else {
            mBackImageView.setVisibility(GONE);
        }
    }

    public void setTitleString(String titleString) {
        mTitleString = titleString;
        mTitleTextView.setText((mTitleString == null) ? "" : mTitleString);
    }

    public void setOnTopBarClickListener(OnTopBarClickListener onTopBarClickListener) {
        mOnTopBarClickListener = onTopBarClickListener;
    }

    public interface OnTopBarClickListener {
        void onBackClicked();
    }
}
