package com.ndboo.wine;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.ndboo.base.BaseActivity;
import com.ndboo.widget.PullUpToLoadMore;
import com.ndboo.widget.TextIndicatorView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商品详情页面
 */
public class GoodsDetailActivity extends BaseActivity {
    private static final int WHAT_GOODS_DETAIL = 1;//获取商品详情
    private static final int WHAT_ADD_TO_ORDER = 2;//加入进货单

    //最外层
    @BindView(R.id.goods_detail_loadmore)
    PullUpToLoadMore mPullUpToLoadMore;

    //带指示器的"商品"和"详情"控件
    @BindView(R.id.goods_header_product)
    TextIndicatorView mProductIndicatorView;
    @BindView(R.id.goods_header_info)
    TextIndicatorView mInfoIndicatorView;

    //轮播
    @BindView(R.id.goods_detail_rollpagerview)
    RollPagerView mPagerView;

    //商品名称、描述、价格、规格、图文
    @BindView(R.id.goods_detail_name)
    TextView mNameTextView;
    @BindView(R.id.goods_detail_description)
    TextView mDescriptionTextView;
    @BindView(R.id.goods_detail_price)
    TextView mPriceTextView;
    @BindView(R.id.goods_detail_unit)
    TextView mUnitTextView;
    @BindView(R.id.goodsdetail_content_image)
    LinearLayout mLinearLayout;

    //消息，供应商，收藏，购物车
    /*
    @BindView(R.id.goods_footer_news)
    ImageTextView mNewsView;
    @BindView(R.id.goods_footer_supplier)
    ImageTextView mSupplierView;
    @BindView(R.id.goods_footer_collection)
    ImageTextView mCollectionView;*/
    @BindView(R.id.goods_footer_cart)
    LinearLayout mCartView;

    //商品id、轮播图、图文介绍、商品名称、售价、规格、描述
    private String mGoodsId;
    private String[] mBannerImage;
    private String[] mContentImage;
    private String mGoodsName;
    private String mGoodsPrice;
    private String mGoodsUnit;
    private String mDescription;

    @OnClick({R.id.goods_header_back, /*R.id.goods_footer_news, R.id.goods_footer_supplier,
            R.id.goods_footer_collection,*/ R.id.goods_footer_cart, R.id.goods_footer_addtocart})
    void doClick(View view) {
        switch (view.getId()) {
            case R.id.goods_header_back:
                Intent backIntent = new Intent();
                backIntent.setAction("backToSort");
                sendBroadcast(backIntent);
                finish();
                break;
            /*case R.id.goods_footer_news:
                //消息
                DialogUtil.showAlertDialog(this);
                break;
            case R.id.goods_footer_supplier:
                //供应商
                DialogUtil.showAlertDialog(this);
                break;
            case R.id.goods_footer_collection:
                //收藏
                DialogUtil.showAlertDialog(this);
                break;*/
            case R.id.goods_footer_cart:
                //进货单
//                Intent intent = new Intent();
                //order表示返回至进货单页面
//                intent.setAction("order");
//                GoodsDetailActivity.this.setResult(MainActivity.RESULT_CODE_ORDER);
                GoodsDetailActivity.this.finish();
//                Constant.finishActivity(SellRankActivity.class.getName());
                break;
            case R.id.goods_footer_addtocart:
                //添加到进货单
                addToOrder();
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_detail;
    }

    @Override
    public void init() {mGoodsId = getIntent().getStringExtra("goodsId");

        initParentView();
        //加载上一页的数据
        initData();
    }

    private void initData() {
    }

    /**
     * 加入进货单
     */
    private void addToOrder() {
    }

    private void initParentView() {
        mProductIndicatorView.setShowIndicator(true);
        mInfoIndicatorView.setShowIndicator(false);
        mPullUpToLoadMore.setOnPageChangeListener(new PullUpToLoadMore.OnPageChangeListener() {
            @Override
            public void pageChanged(int position) {
                if (position == 0) {
                    mProductIndicatorView.setShowIndicator(true);
                    mInfoIndicatorView.setShowIndicator(false);
                } else if (position == 1) {
                    mProductIndicatorView.setShowIndicator(false);
                    mInfoIndicatorView.setShowIndicator(true);

                    //加载下一页的数据。。。
                }
            }
        });
    }

    private void initSubView() {
        mPagerView.setPlayDelay(-1);//禁止自动滚动
        mPagerView.setAnimationDurtion(500);
        mPagerView.setAdapter(new StaticPagerAdapter() {
            @Override
            public View getView(ViewGroup container, int position) {
                ImageView imageView = new ImageView(GoodsDetailActivity.this);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(GoodsDetailActivity.this).load(mBannerImage[position]).into(imageView);
                return imageView;
            }

            @Override
            public int getCount() {
                return mBannerImage.length;
            }
        });
        mNameTextView.setText(mGoodsName);
        mDescriptionTextView.setText(mDescription);
        mPriceTextView.setText(mGoodsPrice + "元");
        mUnitTextView.setText("/" + mGoodsUnit);
        for (int i = 0; i < mContentImage.length; i++) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(params);
            Glide.with(this).load(mContentImage[i]).fitCenter().into(imageView);
            mLinearLayout.addView(imageView);
        }
    }
}
