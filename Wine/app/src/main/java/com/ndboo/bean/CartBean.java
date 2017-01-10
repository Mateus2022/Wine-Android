package com.ndboo.bean;

/**
 * Created by ZhangHang on 2017/1/9.
 */

public class CartBean {


    /**
     * productPicture : 图片地址
     * productCount : 商品数量
     * productPrice : 商品价格
     * productName : 商品名称
     * productId : 商品编号
     * productMoney : 小计
     */

    private String productCount;
    private String productMoney;
    private String productPrice;
    private String productName;
    private String productId;
    private String productPicture;

    public CartBean(String productCount, String productMoney, String productPrice,
                    String productName, String productId, String productPicture) {
        this.productCount = productCount;
        this.productMoney = productMoney;
        this.productPrice = productPrice;
        this.productName = productName;
        this.productId = productId;
        this.productPicture = productPicture;
    }

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public String getProductMoney() {
        return productMoney;
    }

    public void setProductMoney(String productMoney) {
        this.productMoney = productMoney;
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

    public String getProductPicture() {
        return productPicture;
    }

    public void setProductPicture(String productPicture) {
        this.productPicture = productPicture;
    }
}
