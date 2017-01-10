package com.ndboo.bean;

import java.io.Serializable;

/**
 * Created by Li on 2017/1/9.
 * 商品详情
 */

public class WineDetailBean implements Serializable{

    /**
     * imageText : 图文详情
     * pricturePath : 图片地址
     * productRemark : 备注
     * productPrice : 价格
     * productName : 商品名称
     * productId : 商品编号
     */

    private String imageText;
    private String pricturePath;
    private String productRemark;
    private String productPrice;
    private String productName;
    private String productId;

    public String getImageText() {
        return imageText;
    }

    public void setImageText(String imageText) {
        this.imageText = imageText;
    }

    public String getPricturePath() {
        return pricturePath;
    }

    public void setPricturePath(String pricturePath) {
        this.pricturePath = pricturePath;
    }

    public String getProductRemark() {
        return productRemark;
    }

    public void setProductRemark(String productRemark) {
        this.productRemark = productRemark;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "WineDetailBean{" +
                "imageText='" + imageText + '\'' +
                ", pricturePath='" + pricturePath + '\'' +
                ", productRemark='" + productRemark + '\'' +
                ", productPrice='" + productPrice + '\'' +
                ", productName='" + productName + '\'' +
                ", productId='" + productId + '\'' +
                '}';
    }
}
