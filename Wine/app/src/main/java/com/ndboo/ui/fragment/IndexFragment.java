package com.ndboo.ui.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.ndboo.base.BaseFragment;
import com.ndboo.bean.Type;
import com.ndboo.wine.MainActivity;
import com.ndboo.wine.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Li on 2016/12/23.
 * 首页
 */

public class IndexFragment extends BaseFragment {

    @BindView(R.id.grid_view)
    GridView mGridView;
    @BindView(R.id.roll_view_pager)
    RollPagerView mRollPagerView;
    private getWinTypeId mGetWinTypeId;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    public void showContent() {
        super.showContent();
        getCarousel();
        getWineType();
    }

    /**
     * 获取轮播
     */
    public void getCarousel() {
        List<String> colors = Arrays.asList(getResources().getStringArray(R.array.roll_view_pager));
        showCarousel(colors);
    }

    /**
     * 获取酒的种类
     */
    private void getWineType() {
        List<Type> types = new ArrayList<>();

        List<String> strings = Arrays.asList(getResources().getStringArray(R.array.wine_type));
        for (int i = 0; i < strings.size(); i++) {
            Type type = new Type(strings.get(i), R.drawable.ic_type);
            types.add(type);
        }

        showWineType(types);
    }

    /**
     * 显示轮播
     *
     * @param colors 轮播集合
     */
    public void showCarousel(final List<String> colors) {

        mRollPagerView.setPlayDelay(4000);
        mRollPagerView.setAnimationDurtion(500);
        mRollPagerView.setAdapter(new LoopPagerAdapter(mRollPagerView) {
            @Override
            public View getView(ViewGroup container, int position) {
                View view = new View(container.getContext());
                view.setBackgroundColor(Color.parseColor(colors.get(position)));
                return view;
            }


            @Override
            public int getRealCount() {
                return colors.size();

            }
        });


    }

    /**
     * 显示类别
     *
     * @param types 类别集合
     */
    public void showWineType(final List<Type> types) {
        mGridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return types.size();
            }

            @Override
            public Object getItem(int position) {
                return types.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                TypeHolder typeHolder;
                if (convertView == null) {
                    convertView = View.inflate(getContext(), R.layout.item_index_wine_type, null);
                    typeHolder = new TypeHolder(convertView);
                    convertView.setTag(typeHolder);
                } else {
                    typeHolder = (TypeHolder) convertView.getTag();
                }

                Type type = types.get(position);
                typeHolder.mImageView.setImageResource(type.getImgRes());
                typeHolder.mTextView.setText(type.getDescription());
                return convertView;
            }


        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.turnToType();

                if (mGetWinTypeId != null) {
                    mGetWinTypeId.showById(position);

                }
            }
        });

    }

    public void setGetWinTypeId(getWinTypeId getWinTypeId) {
        mGetWinTypeId = getWinTypeId;
    }

    public interface getWinTypeId {
        void showById(int id);
    }

    class TypeHolder {

        @BindView(R.id.image)
        ImageView mImageView;
        @BindView(R.id.text)
        TextView mTextView;

        TypeHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
