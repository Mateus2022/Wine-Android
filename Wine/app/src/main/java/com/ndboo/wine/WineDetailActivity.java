package com.ndboo.wine;

import android.view.View;

import com.ndboo.base.BaseActivity;

import butterknife.OnClick;

public class WineDetailActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_wine_detail;
    }

    @Override
    public void init() {

    }

    @OnClick({R.id.top_bar_back, R.id.activity_wine_detail})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_bar_back:
                finish();
                break;
            case R.id.activity_wine_detail:
                break;
        }
    }
}
