package com.ndboo.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by ZhangHang on 2016/8/11.
 * 弹框工具类
 */
public class ToastUtil {
    private static Toast mToast;

    /**
     * 短时间提示
     *
     * @param activity
     * @param text
     */
    public static void showToast(Activity activity, String text) {
        if (null == activity || text == null || text.equals("")) {
            return;
        }
        if (null == mToast) {
            mToast = Toast.makeText(activity, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }
}
