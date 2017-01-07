package com.ndboo.wine;

import com.ndboo.base.BaseActivity;
import com.ndboo.widget.TopBar;

import butterknife.BindView;

public class WineDetailActivity extends BaseActivity {


    @BindView(R.id.top_bar_back)
    TopBar mTopBarBack;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wine_detail;
    }

    @Override
    public void init() {
        mTopBarBack.setOnTopBarClickListener(new TopBar.OnTopBarClickListener() {
            @Override
            public void onBackClicked() {
                finish();
            }
        });
    }


}
