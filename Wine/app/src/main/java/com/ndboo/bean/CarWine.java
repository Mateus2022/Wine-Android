package com.ndboo.bean;

/**
 * Created by Li on 2017/1/6.
 * “购物车”实体
 */

public class CarWine {
    private boolean isSelected;

    public CarWine(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
