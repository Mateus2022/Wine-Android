package com.ndboo.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Toast显示工具
 * Created by Li on 2016/8/10.
 */
public class RemindUtil {
    private static String oldMessage;
    private static Toast toast = null;
    private static long beforeTime = 0;
    private static long nowTime = 0;
    public static void showToast(Context context, String message, boolean isCenter){
        if (toast == null){
            toast = Toast.makeText(context.getApplicationContext(),message, Toast.LENGTH_SHORT);
            if (isCenter){
                toast.setGravity(Gravity.CENTER, 0, 0);
            }else {
                toast.setGravity(Gravity.BOTTOM, 0, 0);
            }
            toast.show();
            beforeTime = System.currentTimeMillis();
        }else {
            nowTime = System.currentTimeMillis();
            if (message.equals(oldMessage)) {
                if (nowTime - beforeTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMessage = message;
                toast.setText(message);
                toast.show();
            }
        }
        beforeTime = nowTime;
    }


    private static AlertDialog.Builder builder;

    /**
     * 对话框提示
     * @param activity 上下文
     * @param messageId 显示内容编号
     */
    public static void dialogRemind(Activity activity, int messageId){
        builder = new AlertDialog.Builder(activity);
        builder.setMessage(messageId);
        builder.setPositiveButton("确定",null);
        builder.create().show();
     }
}
