package com.example.gaodemapdemo.model;

import android.content.Context;
import android.util.Log;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import java.util.List;

public class GeoSearchTool {
    private Context context;

    public GeoSearchTool(Context context) {
        this.context = context;
    }

    // 逆地理查询的回调方法
    public interface onReGeocodeSearchCallback {
        void onSuccess(RegeocodeAddress regeocodeAddress); // 成功则返回逆地理查询的结果
        void onFailure(String errorMessage); // 失败则输出错误提示
    }

    // （正）地理查询的回调方法
    public interface onGeocodeSearchCallback {
        void onSuccess(GeocodeAddress geocodeAddress); // 成功则返回正地理查询的结果
        void onFailure(String errorMessage); // 失败则输出错误提示
    }

    // 执行逆地理查询：根据坐标查询地址
    public void onReGeocodeSearch(LatLonPoint latLonPoint, onReGeocodeSearchCallback callback) {
        // 初始化GeocodeSearch对象
        com.amap.api.services.geocoder.GeocodeSearch geocodeSearch;
        try {
            geocodeSearch = new com.amap.api.services.geocoder.GeocodeSearch(context);
        } catch (AMapException e) {
            throw new RuntimeException(e);
        }

        // 构造逆地理编码参数————设置逆地理编码的范围为20，单位米
        RegeocodeQuery regeocodeQuery = new RegeocodeQuery(latLonPoint, 20, com.amap.api.services.geocoder.GeocodeSearch.AMAP);

        // 设置监听器
        geocodeSearch.setOnGeocodeSearchListener(new com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                // 逆地理查询：根据经纬度坐标查询出地址等信息
                if (i == AMapException.CODE_AMAP_SUCCESS) {
                    // 获取逆地理查询的结果，结果保存在regeocodeAddress中
                    RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
                    callback.onSuccess(regeocodeAddress);
                } else {
                    callback.onFailure("Failed to RegeocodeSearch");
                    Log.e("Location", "Failed to get Location. ResultCode: " + i);
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                // （正）地理查询：根据地址查询到经纬度坐标
            }
        });

        // 启动逆地理查询
        geocodeSearch.getFromLocationAsyn(regeocodeQuery);
    }

    // （正）地理查询：根据地址查询到经纬度坐标
    public void onGeocodeSearch(String address, String city, onGeocodeSearchCallback callback) {
        // 初始化geocodesearch对象
        com.amap.api.services.geocoder.GeocodeSearch geocodeSearch;
        try {
            geocodeSearch = new com.amap.api.services.geocoder.GeocodeSearch(context);
        } catch (AMapException e) {
            throw new RuntimeException(e);
        }

        // address表示地址，第二个参数表示查询城市，中文或者中文全拼，cityCode、adCode，null表示在全国范围内查询
        GeocodeQuery geocodeQuery = new GeocodeQuery(address, city);

        // 添加日志，检查逆地址查询参数
        Log.d("AddressInput", "开始地理查询，地址：" + address);

        geocodeSearch.setOnGeocodeSearchListener(new com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                // 逆地理查询：根据经纬度坐标查询出地址等信息
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                // （正）地理查询：根据地址查询到经纬度坐标
                if (i == AMapException.CODE_AMAP_SUCCESS) {
                    List<GeocodeAddress> geocodeAddressList = geocodeResult.getGeocodeAddressList();
                    if (geocodeAddressList != null && geocodeAddressList.size() > 0) {
                        // 获取查询结果
                        GeocodeAddress geocodeAddress = geocodeAddressList.get(0);
                        callback.onSuccess(geocodeAddress);
                    } else {
                        Log.e("onGeocodeSearch", "geocodeAddressList为空！");
                    }
                } else {
                    callback.onFailure("没有查询到结果！");
                    Log.e("onGeocodeSearch", "Failed to onGeocodeSearch. ResultCode: " + i);
                }
            }
        });
        // 启动地理查询
        geocodeSearch.getFromLocationNameAsyn(geocodeQuery);
    }


}
