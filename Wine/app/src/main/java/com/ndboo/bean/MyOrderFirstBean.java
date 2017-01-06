package com.ndboo.bean;

/**
 * Created by ZhangHang on 2016/8/22.
 */
public class MyOrderFirstBean {
    private String orderId;
    //下单时间
    private String placeTime;
    //订单内商品种类
    private String count;
    //订单总价
    private String price;
    //状态(已支付、已取消...)
    private String status;

    public MyOrderFirstBean(String orderId, String placeTime,
                            String count, String price, String status) {
        this.orderId = orderId;
        this.placeTime = placeTime;
        this.count = count;
        this.price = price;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPlaceTime() {
        return placeTime;
    }

    public void setPlaceTime(String placeTime) {
        this.placeTime = placeTime;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MyOrderFirstBean{" +
                "placeTime='" + placeTime + '\'' +
                '}';
    }
}
