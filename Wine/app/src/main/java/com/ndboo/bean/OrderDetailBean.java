package com.ndboo.bean;

/**
 * Created by ZhangHang on 2016/9/9.
 */
public class OrderDetailBean {
    //商品图片，名称、数量、单价、小计
    private String imagePath;
    private String name;
    private String count;
    private String perPrice;
    private String total;

    public OrderDetailBean(String imagePath, String name, String count,
                           String perPrice, String total) {
        this.imagePath = imagePath;
        this.name = name;
        this.count = count;
        this.perPrice = perPrice;
        this.total = total;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
