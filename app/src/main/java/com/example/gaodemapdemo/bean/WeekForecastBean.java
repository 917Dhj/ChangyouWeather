package com.example.gaodemapdemo.bean;

public class WeekForecastBean {
    private String date;
    private int icon;
    private String tempMax;
    private String tempMin;

    public WeekForecastBean(String date, int icon, String tempMax, String tempMin) {
        this.date = date;
        this.icon = icon;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }
}
