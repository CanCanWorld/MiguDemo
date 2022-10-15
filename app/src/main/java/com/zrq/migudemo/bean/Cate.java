package com.zrq.migudemo.bean;

public class Cate {
    private String name;
    private String id;
    private int resId;

    public Cate(String name, String id, int resId) {
        this.name = name;
        this.id = id;
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
