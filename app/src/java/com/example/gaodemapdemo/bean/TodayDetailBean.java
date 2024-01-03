package com.example.gaodemapdemo.bean;

public class TodayDetailBean {
    private int Icon;
    private String value;
    private String name;

    public TodayDetailBean(int icon, String value, String name) {
        Icon = icon;
        this.value = value;
        this.name = name;
    }

    public int getIcon() {
        return Icon;
    }

    public void setIcon(int icon) {
        Icon = icon;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
