package com.ndboo.wine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.ndboo.widget.TopBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProtocolActivity extends AppCompatActivity {

    @BindView(R.id.top_bar)
    TopBar mTopBar;
    @BindView(R.id.web_protocol)
    WebView mWebProtocol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mTopBar.setOnTopBarClickListener(new TopBar.OnTopBarClickListener() {
            @Override
            public void onBackClicked() {
                finish();
            }
        });
        mWebProtocol.loadUrl("http://www.ndboo.com/NDBwebApp/UserAgreement.html");
        mWebProtocol.getSettings().setJavaScriptEnabled(true);

    }
}
