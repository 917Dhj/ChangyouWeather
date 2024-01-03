package com.example.gaodemapdemo.model;

import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;
import com.amap.api.maps.model.LatLng;
import com.example.gaodemapdemo.MainActivity;

public class InitLocation implements AMapLocationListener {
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private MainActivity mainActivity;

    public InitLocation(AMapLocationClient mLocationClient, AMapLocationClientOption mLocationOption, MainActivity mainActivity) {
        this.mLocationClient = mLocationClient;
        this.mLocationOption = mLocationOption;
        this.mainActivity = mainActivity;
    }

    /**
     * 初始化定位
     */
    public void initLocation() {
        if (mLocationClient != null) {
            //设置定位回调监听，this通过当前类来实现定位结果的监听
            mLocationClient.setLocationListener(this);
            //初始化AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            mLocationOption.setOnceLocationLatest(true);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置定位请求超时时间，单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption.setHttpTimeOut(20000);
            //关闭缓存机制，高精度定位会产生缓存。
            mLocationOption.setLocationCacheEnable(false);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
        }
    }

    /**
     * 接收异步返回的定位结果
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //地址
                String address = aMapLocation.getAddress();
                //获取纬度
                double latitude = aMapLocation.getLatitude();
                //获取经度
                double longitude = aMapLocation.getLongitude();
                //将当前定位的经纬度保存到LatLng对象currLatLng
                LatLng currLatLng = new LatLng(latitude, longitude);
                Log.d("Latlng", latitude + "," + longitude);
                // 将当前定位的经纬度封装成String类型的经纬度数据location，用于天气查询
                String location = String.format("%.6f,%.6f", longitude, latitude);
                Log.d("Location", location);
                //赋值当前定位所在的城市
                String currCity = aMapLocation.getCity();
                // 赋值当前定位所在的地区
                String district = aMapLocation.getDistrict();

                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("纬度：" + latitude + "\n");
                stringBuffer.append("经度：" + longitude + "\n");
                stringBuffer.append("地址：" + address + "\n");

                Log.d("MainActivity", stringBuffer.toString());

                //停止定位后，本地定位服务并不会被销毁
                mLocationClient.stopLocation();

                //在主线程中更新相应的UI
                mainActivity.LocationRespond(address, location, currCity, district, aMapLocation);

            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }
}
