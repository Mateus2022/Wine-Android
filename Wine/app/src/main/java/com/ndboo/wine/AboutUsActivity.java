package com.ndboo.wine;

import com.ndboo.base.BaseActivity;
import com.ndboo.widget.TopBar;

import butterknife.BindView;

public class AboutUsActivity extends BaseActivity {
    @BindView(R.id.aboutus_topbar)
    TopBar mTopBar;


    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    public void init() {
        addListener();
    }

    private void addListener() {
        mTopBar.setOnTopBarClickListener(new TopBar.OnTopBarClickListener() {
            @Override
            public void onBackClicked() {
                finish();
            }
        });
    }
}
