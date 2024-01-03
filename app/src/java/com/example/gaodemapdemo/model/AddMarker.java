package com.example.gaodemapdemo.model;

import android.os.Handler;
import android.os.Looper;
import android.view.animation.LinearInterpolator;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.ScaleAnimation;


public class AddMarker {
    // 此类封装了在地图上添加标点的方法
    private AMap aMap;

    public AddMarker(AMap aMap) {
        this.aMap = aMap;
    }

    /**
     * 添加地图标点
     *
     * @param latLng
     */
    public void addMarker(LatLng latLng) {
        //每次点击地图时先清除之前的标点，再添加新的标点，true表示只清除自定义的标点，不包括定位蓝点
        aMap.clear(true);
        //添加标点
        Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("地址：").snippet(""));

        // 创建一个放大的ScaleAnimation对象，参数分别是：起始横向缩放倍数、结束横向缩放倍数、起始纵向缩放倍数、结束纵向缩放倍数
        ScaleAnimation scaleUpAnimation = new ScaleAnimation(1f, 2f, 1f, 2f);
        long durationUp = 250L;  // 动画时长，单位毫秒
        scaleUpAnimation.setDuration(durationUp);
        scaleUpAnimation.setInterpolator(new LinearInterpolator());

        // 创建一个使大小保持的 ScaleAnimation 对象
        ScaleAnimation scaleStayAnimation = new ScaleAnimation(2f, 2f, 2f, 2f);
        long durationDown = 250L;  // 缩小动画时长，单位毫秒
        scaleStayAnimation.setDuration(durationDown);
        scaleStayAnimation.setInterpolator(new LinearInterpolator());

        // 将放大动画设置给标点对象
        marker.setAnimation(scaleUpAnimation);
        marker.startAnimation();

        //使用Handler延迟执行使标点保持大小的动画，延迟为durationUp也就是放大动画的时长
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                marker.setAnimation(scaleStayAnimation);
                marker.startAnimation();
            }
        }, durationUp);
    }
}
