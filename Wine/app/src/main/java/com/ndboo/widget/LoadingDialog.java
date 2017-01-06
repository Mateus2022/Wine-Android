package com.ndboo.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.ndboo.wine.R;

/**
 * Created by ZhangHang on 2016/9/5.
 */
public class LoadingDialog extends ProgressDialog {
    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_lodingdialog);
        // 设置ProgressDialog 的进度条是否不明确
        setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        setCancelable(false);
    }
}
