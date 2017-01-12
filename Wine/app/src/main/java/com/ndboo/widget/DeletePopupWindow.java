package com.ndboo.widget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ndboo.wine.R;


/**
 * Created by ZhangHang on 2016/8/26.
 */
public class DeletePopupWindow extends PopupWindow {
    private View mView;
    private TextView mMessageTextView;
    private Button mCancleButton, mEnsureButton;
    private Activity mActivity;

    //提示文字
    private String mMessageText = "确认删除所选商品吗";
    //确认、取消文字
    private String mCancleText = "取消", mEnsureText = "确认";

    private OnPopupWindowClickListener mOnPopupWindowClickListener;

    public DeletePopupWindow(Activity activity) {
        mActivity = activity;
        setOutsideTouchable(false);

        mView = LayoutInflater.from(activity).inflate(R.layout.layout_myalertdialog, null);
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
                    dismiss();
                }
            }
        });

        mEnsureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnPopupWindowClickListener != null) {
                    mOnPopupWindowClickListener.ensureClicked(mEnsureButton);
                    dismiss();
                }
            }
        });
        setContentView(mView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        setBackgroundAlpha(0.4f);
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void dismiss() {
        setBackgroundAlpha(1f);
        super.dismiss();
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

    /**
     * 设置屏幕的背景透明度
     */
    private void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mActivity.getWindow().setAttributes(lp);
    }
}
