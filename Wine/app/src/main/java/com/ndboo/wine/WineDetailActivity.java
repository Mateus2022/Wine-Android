package com.ndboo.wine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
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
    LinearLayout mLayoutTop;
    @BindView(R.id.tv_img_text)
    TextView mTvImgText;
    @BindView(R.id.layout_image_text)
    LinearLayout mLayoutImageText;
    private String productId;
    private int mLayoutTopHeight;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wine_detail;
    }

    @Override
    public void init() {
        productId = getIntent().getStringExtra("wineId");
        Subscription subscription = RetrofitHelper.getApi()
                .showWineDetail(productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<WineDetailBean>() {
                    @Override
                    public void call(WineDetailBean wineDetailBean) {
                        final String[] wineImages = wineDetailBean.getPricturePath().split(",");
                        mGoodsDetailViewPager.setAdapter(new StaticPagerAdapter() {
                            @Override
                            public View getView(ViewGroup container, int position) {
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
        mLayoutImageText.setPadding(0, mLayoutTopHeight + 5, 0, 0);
        mLayoutContent.setiChanged(new PullUpToLoadMore.IChanged() {
            @Override
            public void changed(int t) {
                if (t <= mLayoutTopHeight) {
                    mTvImgText.setVisibility(View.INVISIBLE);
                    mIvCar.setImageResource(R.drawable.ic_detail_car_no_bg);
                    mIvBack.setImageResource(R.drawable.ic_detail_back_no_bg);
                    mLayoutTop.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                } else {
                    mTvImgText.setVisibility(View.VISIBLE);
                    mIvCar.setImageResource(R.drawable.ic_detail_car_has_bg);
                    mIvBack.setImageResource(R.drawable.ic_detail_back_has_bg);
                    mLayoutTop.setBackgroundColor(getResources().getColor(R.color.themeColor));
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

                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_car:
                setResult(2);
                finish();
                break;
            case R.id.layout_help:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "051266155111"));
                startActivity(intent);
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

}
