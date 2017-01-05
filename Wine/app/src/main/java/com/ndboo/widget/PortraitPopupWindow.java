package com.ndboo.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ndboo.wine.R;

/**
 * Created by ZhangHang on 2017/1/5.
 */

public class PortraitPopupWindow extends PopupWindow {
    private View mContentView; // PopupWindow 菜单布局
    private Context mContent; // 上下文参数
    private OnPopClickListener mOnPopClickListener;//点击事件

    public PortraitPopupWindow(Context context) {
        this.mContent = context;
        mContentView = LayoutInflater.from(mContent)
                .inflate(R.layout.layout_popupwindow_portrait, null);
        init();
    }

    private void init() {
        TextView takePicTextView = (TextView) mContentView.findViewById(R.id.portrait_take);
        TextView choosePicTextView = (TextView) mContentView.findViewById(R.id.portrait_choose);
        TextView cancleTextView = (TextView) mContentView.findViewById(R.id.portrait_cancle);
        takePicTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                mOnPopClickListener.onTakePicClicked();
            }
        });

        choosePicTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                mOnPopClickListener.onChoosePicClicked();
            }
        });

        cancleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        //导入布局
        setContentView(mContentView);
        // 设置动画效果
        this.setAnimationStyle(R.style.AnimationFade);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setOutsideTouchable(false);
    }

    public void setOnPopClickListener(OnPopClickListener onPopClickListener) {
        mOnPopClickListener = onPopClickListener;
    }

    public interface OnPopClickListener {
        void onTakePicClicked();

        void onChoosePicClicked();
    }
}
