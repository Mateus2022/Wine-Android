package com.ndboo.interfaces;

import android.view.View;

/**
 * Created by Li on 2017/1/7.
 * 商城和购物车界面数量操作点击接口
 */

public interface NumOperationListener {
    void numAdd(int position, View view);
    void numReduce(int position,View view);
    int position();
}
