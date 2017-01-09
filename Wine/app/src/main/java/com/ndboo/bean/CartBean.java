package com.ndboo.bean;

/**
 * Created by ZhangHang on 2017/1/9.
 */

public class CartBean {
    /**
     * picPath : 图片地址
     * cartProductCount : 商品数量
     * productPrice : 商品价格
     * productName : 商品名称
     * productId : 商品编号
     * totalPrice : 小计
     */

    private String picPath;
    private String cartProductCount;
    private String productPrice;
    private String productName;
    private String productId;
    private String totalPrice;

    public CartBean(String picPath, String cartProductCount, String productPrice,
                    String productName, String productId, String totalPrice) {
        this.picPath = picPath;
        this.cartProductCount = cartProductCount;
        this.productPrice = productPrice;
        this.productName = productName;
        this.productId = productId;
        this.totalPrice = totalPrice;
    }

    public String getPicPath() {
        return picPath;
    }

    public String getCartProductCount() {
        return cartProductCount;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductId() {
        return productId;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        return "CartBean{" +
                "picPath='" + picPath + '\'' +
                ", cartProductCount='" + cartProductCount + '\'' +
                ", productPrice='" + productPrice + '\'' +
                ", productName='" + productName + '\'' +
                ", productId='" + productId + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                '}';
    }
}
