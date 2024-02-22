package com.example.recomap;

import com.google.android.gms.maps.model.LatLng;

public class ShopInfo {
    private int category;
    private String name;
    private LatLng position;
    private String infomation;
    private int star;

    public void setIntcategory(int intcategory) {
        this.category = intcategory;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public void setInfomation(String infomation) {
        this.infomation = infomation;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getIntcategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public LatLng getPosition() {
        return position;
    }

    public String getInfomation() {
        return infomation;
    }

    public int getStar() {
        return star;
    }
}
