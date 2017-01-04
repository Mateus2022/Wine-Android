package com.ndboo.bean;

/**
 * Created by Administrator on 2016/12/23.
 */

public class Type {
    private String description;
    private int imgRes;




    public Type(String description, int imgRes) {
        this.description = description;
        this.imgRes = imgRes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }
}
