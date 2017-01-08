package com.ndboo.bean;

/**
 * Created by ZhangHang on 2016/8/22.
 */
public class OrderSecondBean {
    //图片资源
    private String imageUrl;
    //商品名称
    private String name;
    //商品数量
    private String count;
    //商品单价
    private String perPrice;
    //单位
    private String unit;
    //商品小计
    private String totalPrice;

    public OrderSecondBean(String imageUrl, String name, String count,
                           String perPrice, String unit, String totalPrice) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.count = count;
        this.perPrice = perPrice;
        this.unit = unit;
        this.totalPrice = totalPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPerPrice() {
        return perPrice;
    }

    public void setPerPrice(String perPrice) {
        this.perPrice = perPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OrderSecondBean) {
            OrderSecondBean bean = (OrderSecondBean) obj;
            return bean.name.equals(name);
        }
        return super.equals(obj);
    }
}
