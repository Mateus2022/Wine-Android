package com.ndboo.wine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jauker.widget.BadgeView;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.ndboo.base.BaseActivity;
import com.ndboo.bean.WineDetailBean;
import com.ndboo.net.RetrofitHelper;
import com.ndboo.utils.ScreenUtil;
import com.ndboo.utils.SharedPreferencesUtil;
import com.ndboo.utils.ToastUtil;
import com.ndboo.widget.PullUpToLoadMore;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 酒详情页面
 */
public class WineDetailActivity extends BaseActivity {


    @BindView(R.id.tv_car)
    TextView mTvCar;
    @BindView(R.id.text_buy)
    TextView mTextBuy;
    @BindView(R.id.goods_detail_view_pager)
    RollPagerView mGoodsDetailViewPager;
    @BindView(R.id.goods_detail_name)
    TextView mGoodsDetailName;
    @BindView(R.id.goods_detail_price)
    TextView mGoodsDetailPrice;
    @BindView(R.id.text_product_name)
    TextView mTextProductName;
    @BindView(R.id.goods_detail_description)
    TextView mGoodsDetailDescription;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_car)
    ImageView mIvCar;
    @BindView(R.id.layout_content)
    PullUpToLoadMore mLayoutContent;
    @BindView(R.id.layout_top)
    RelativeLayout mLayoutTop;
    @BindView(R.id.layout_image_text)
    LinearLayout mLayoutImageText;
    @BindView(R.id.tv_product)
    TextView mTvProduct;
    @BindView(R.id.tv_detail)
    TextView mTvDetail;

    private String productId;
    private int mLayoutTopHeight;
    private WineDetailBean mWineDetailBean;

    private double mPrice = 0;
    private String mProductCount;
    private BadgeView mBadgeView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wine_detail;
    }

    @Override
    public void init() {
        productId = getIntent().getStringExtra("wineId");
        mBadgeView = new BadgeView(WineDetailActivity.this);
        mBadgeView.setBadgeMargin(5);
        mBadgeView.setTargetView(mIvCar);
        Subscription subscription = RetrofitHelper.getApi()
                .showWineDetail(productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<WineDetailBean>() {
                    @Override
                    public void call(WineDetailBean wineDetailBean) {
                        mWineDetailBean = wineDetailBean;
                        final String[] wineImages = wineDetailBean.getPricturePath().split(",");
                        mWineDetailBean.setPricturePath(wineImages[0]);
                        mGoodsDetailViewPager.setAdapter(new StaticPagerAdapter() {
                            @Override
                            public View getView(ViewGroup container, int position) {
                                queryWineNum();
                                ImageView imageView = new ImageView(getApplicationContext());
                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                Glide.with(getApplicationContext())
                                        .load(wineImages[position])
                                        .into(imageView);
                                return imageView;
                            }

                            @Override
                            public int getCount() {
                                return wineImages.length;
                            }
                        });

                        mGoodsDetailName.setText(wineDetailBean.getProductName());
                        mGoodsDetailPrice.setText("¥：" + wineDetailBean.getProductPrice());
                        mGoodsDetailDescription.setText(wineDetailBean.getProductRemark());
                        mPrice = Double.parseDouble(wineDetailBean.getProductPrice());
                        String[] imgTexts = wineDetailBean.getImageText().split(",");
                        for (final String imgText : imgTexts) {
                            final ImageView imageView = new ImageView(getApplicationContext());
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            Glide.with(getApplicationContext())
                                    .load(imgText)
                                    .asBitmap()
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                            int imageWidth = resource.getWidth();
                                            int imageHeight = resource.getHeight();
                                            int height = ScreenUtil.getScreenWidth(getApplicationContext()) * imageHeight / imageWidth;
                                            ViewGroup.LayoutParams para = imageView.getLayoutParams();
                                            para.height = height;
                                            imageView.setLayoutParams(para);
                                            Glide.with(getApplicationContext()).load(imgText).asBitmap().into(imageView);
                                        }
                                    });
                            mLayoutImageText.addView(imageView);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
        addSubscription(subscription);
        mLayoutTop.measure(0, 0);

        mLayoutTopHeight = mLayoutTop.getMeasuredHeight();
//        mLayoutImageText.setPadding(0, mLayoutTopHeight + 5, 0, 0);
        mLayoutContent.setiChanged(new PullUpToLoadMore.IChanged() {
            @Override
            public void changed(int t) {
                if (t <= mLayoutTopHeight) {
                    mTvProduct.setBackgroundResource(R.drawable.layer_list_wine_detail_select);
                    mTvDetail.setBackgroundResource(R.color.themeColor);
                } else {
                    mTvDetail.setBackgroundResource(R.drawable.layer_list_wine_detail_select);
                    mTvProduct.setBackgroundResource(R.color.themeColor);
                }
            }
        });
    }

    @OnClick({R.id.tv_car, R.id.text_buy, R.id.iv_back, R.id.iv_car, R.id.layout_help})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_car:
                addToCar();
                break;
            case R.id.text_buy:
                if (!SharedPreferencesUtil.isUserLoginIn(getApplicationContext())) {
                    startActivity(new Intent(WineDetailActivity.this, LoginActivity.class));
                } else {
                    if (mPrice == 0) {
                        return;
                    } else {
                        /*if (mPrice < 100) {
                            AlertDialog.Builder builder=new AlertDialog.Builder(WineDetailActivity.this)
                                    .setTitle("温馨提示")
                                    .setMessage("商品价格不满100,是否添加至购物车")
                                    .setNegativeButton("取消",null)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            addToCar();
                                        }
                                    });
                            builder.create().show();
                        }else {*/
                        Intent payIntent = new Intent(this, EditOrderActivity.class);
                        //获取id
                        payIntent.putExtra("wine", mWineDetailBean);
                        payIntent.putExtra("type", 2);
                        startActivity(payIntent);
//                        }
                    }

                }

                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_car:
                if (!SharedPreferencesUtil.isUserLoginIn(getApplicationContext())) {
                    startActivity(new Intent(WineDetailActivity.this, LoginActivity.class));
                } else {
                    setResult(2);
                    finish();
                }

                break;
            case R.id.layout_help:
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                phoneIntent.setData(Uri.parse("tel:" + "051266155111"));
                startActivity(phoneIntent);
                break;
        }
    }

    private void addToCar() {
        if (!SharedPreferencesUtil.isUserLoginIn(getApplicationContext())) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            mTvCar.setEnabled(false);
            Subscription subscription = RetrofitHelper.getApi()
                    .addWineToCar(SharedPreferencesUtil.getUserId(getApplicationContext()), productId, "1")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            mTvCar.setEnabled(true);
                            try {
                                queryWineNum();
                                JSONObject object = new JSONObject(s);
                                String result = object.getString("result");
                                if (result.equals("true")) {
                                    setResult(3);
                                    ToastUtil.showToast(WineDetailActivity.this, "添加成功");
                                } else {
                                    ToastUtil.showToast(WineDetailActivity.this, "添加失败");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            mTvCar.setEnabled(true);
                            ToastUtil.showToast(WineDetailActivity.this, "请检查网络连接");
                        }
                    });
            addSubscription(subscription);
        }
    }

    private void queryWineNum() {
        Subscription subscription = RetrofitHelper.getApi()
                .queryCarNum(SharedPreferencesUtil.getUserId(getApplicationContext()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            mProductCount = object.getString("productCount");
                            if (mBadgeView != null) {
                                mBadgeView.setText(mProductCount);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });

        addSubscription(subscription);
    }


}
