package com.ndboo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ndboo.wine.R;

/**
 * Created by ZhangHang on 2016/8/26.
 */
public class PhonePopupWindow extends PopupWindow {
    private View mView;
    private TextView mMessageTextView;
    private Button mCancleButton, mEnsureButton;

    //提示文字
    private String mMessageText = "您确认取消订单";
    //确认、取消文字
    private String mCancleText = "取消", mEnsureText = "确认";

    private OnPopupWindowClickListener mOnPopupWindowClickListener;

    public PhonePopupWindow(Context context) {
        this(context, null);
    }

    public PhonePopupWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhonePopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOutsideTouchable(false);

        mView = LayoutInflater.from(context).inflate(R.layout.layout_popupwindow_alertdialog, null);
        mMessageTextView = (TextView) mView.findViewById(R.id.popupwindow_message);
        mCancleButton = (Button) mView.findViewById(R.id.popupwindow_cancle);
        mEnsureButton = (Button) mView.findViewById(R.id.popupwindow_ensure);

        mCancleButton.setText(mCancleText);
        mMessageTextView.setText(mMessageText);
        mEnsureButton.setText(mEnsureText);

        mCancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnPopupWindowClickListener != null) {
                    mOnPopupWindowClickListener.cancleClicked(mCancleButton);
                    PhonePopupWindow.this.dismiss();
                }
            }
        });

        mEnsureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnPopupWindowClickListener != null) {
                    mOnPopupWindowClickListener.ensureClicked(mEnsureButton);
                    PhonePopupWindow.this.dismiss();
                }
            }
        });
        setContentView(mView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public String getMessageText() {
        return mMessageText;
    }

    public void setMessageText(String messageText) {
        mMessageText = messageText;
        mMessageTextView.setText(mMessageText);
    }

    public String getCancleText() {
        return mCancleText;
    }

    public String getEnsureText() {
        return mEnsureText;
    }

    public void setEnsureText(String ensureText) {
        mEnsureText = ensureText;
        mEnsureButton.setText(mEnsureText);
    }

    public void setCancleText(String cancleText) {
        mCancleText = cancleText;
        mCancleButton.setText(mCancleText);
    }

    public interface OnPopupWindowClickListener {
        void cancleClicked(View view);

        void ensureClicked(View view);
    }

    public void setOnPopupWindowClickListener(OnPopupWindowClickListener onPopupWindowClickListener) {
        mOnPopupWindowClickListener = onPopupWindowClickListener;
    }
}
