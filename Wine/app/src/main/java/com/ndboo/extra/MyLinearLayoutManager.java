package com.ndboo.extra;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MyLinearLayoutManager extends LinearLayoutManager {

    public MyLinearLayoutManager(Context context) {
        super(context);
    }

    public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state,
                          int widthSpec, int heightSpec) {
        int measuredWidth = 0, measuredHeight = 0;

        int size = recycler.getScrapList().size();
        for (int i = 0; i < size; i++) {
            View view = recycler.getViewForPosition(0);
            if (view != null) {
                measureChild(view, widthSpec, heightSpec);
                measuredWidth = Math.max(View.MeasureSpec.getSize(widthSpec), measuredWidth);
                measuredHeight += view.getMeasuredHeight();
            }
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}