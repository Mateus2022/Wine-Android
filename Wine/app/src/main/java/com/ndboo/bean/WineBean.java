package com.ndboo.bean;

/**
 * Created by Li on 2017/1/5.
 * 酒水实体
 */

public class WineBean {
    /**
     * picPath : 图片地址
     * cartProductCount : 商品数量
     * productPrice : 商品价格
     * productName : 商品名称
     * productId : 商品编号
     */

    private String picPath;
    private String cartProductCount;
    private String productPrice;
    private String productName;
    private String productId;

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getCartProductCount() {
        return cartProductCount;
    }

    public void setCartProductCount(String cartProductCount) {
        this.cartProductCount = cartProductCount;
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
        return getProductId()+" "+getProductName()+" "+getCartProductCount();
    }

}
