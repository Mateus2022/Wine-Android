package com.ndboo.interfaces;

import android.view.View;

/**
 * Created by Li on 2017/1/6.
 * 购物车界面点击事件接口
 */

public interface ShoppingCarOnItemClickListener {

    void numAdd(int position, View view);

    void numReduce(int position, View view);

    void viewClick(int position, View view);

    void onAllChanged(boolean isChanged);
}
