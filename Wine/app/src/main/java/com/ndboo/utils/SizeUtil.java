package com.ndboo.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Li on 2016/12/30.
 */

public class SizeUtil {

    public static float dp2px(float value, Context context){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,
                context.getApplicationContext().getResources().getDisplayMetrics());
    }

    public static float sp2px(float value,Context context){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,value,
                context.getApplicationContext().getResources().getDisplayMetrics());
    }


}
