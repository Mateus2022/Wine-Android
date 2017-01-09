package com.ndboo.utils;

import android.content.Context;

/**
 * Created by Li on 2017/1/9.
 * 屏幕尺寸相关
 */

public class ScreenUtil {
    public static int getScreenWidth(Context context){
        return context.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
    }
}
