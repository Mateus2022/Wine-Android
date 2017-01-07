package com.ndboo.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Li on 2017/1/7.
 * SharedPreferences工具类
 */

public class SharedPreferencesUtil {

    private static final String USER_INFO = "user_info";


    private static final String USER_ID="user_id";
    private static final String USER_ACCOUNT="user_account";


    private static SharedPreferences userInfoSharePreferences;
    private static SharedPreferences getUserInfoSharePreferences(Context context){
        if (userInfoSharePreferences != null) {
            userInfoSharePreferences=context
                    .getApplicationContext()
                    .getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        }
        return userInfoSharePreferences;
    }

    /**
     * 保存用户信息
     * @param context   上下文
     * @param userId    用户编号
     * @param userAccount  用户账号
     */
    public static void saveUserInfo(Context context, String userId, String userAccount) {

        SharedPreferences.Editor editor = getUserInfoSharePreferences(context).edit();
        editor.putString(USER_ID, userId);
        editor.putString(USER_ACCOUNT, userAccount);
        editor.apply();
    }

    /**
     * 获取用户编号
     * @param context   上下文
     * @return  用户编号
     */
    public static String getUserId(Context context){
        return getUserInfoSharePreferences(context).getString(USER_ID,"-1");
    }
}
