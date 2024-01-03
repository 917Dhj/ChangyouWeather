package com.example.gaodemapdemo.model;

import android.widget.TextView;

import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;

public class InitCityPicker {
    private CityPickerView mPicker;
    private TextView tvChooseCity;

    public InitCityPicker(CityPickerView mPicker, TextView tvChooseCity) {
        this.mPicker = mPicker;
        this.tvChooseCity = tvChooseCity;
    }

    //配置城市选择器
    public void initCityPicker() {
        //自定义配置
        CityConfig cityConfig = new CityConfig.Builder()
                .setCityWheelType(CityConfig.WheelType.PRO_CITY) //显示类，只显示省份一级，显示省市两级还是显示省市区三级
                .title("选择城市") //标题
                .showBackground(true) //是否显示半透明背景
                .province("北京市") //默认显示的省份
                .city("北京市") //默认显示省份下面的城市
                .provinceCyclic(false) //省份滚轮是否可以循环滚动
                .cityCyclic(false) //城市滚轮是否可以循环滚动
                .setShowGAT(true) //是否显示港澳台数据，默认不显示
                .build();
        //设置配置
        mPicker.setConfig(cityConfig);
        //监听选择点击事件及返回结果
        mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                //这里可以获取到选择的结果
                super.onSelected(province, city, district);
                //获取选择到的城市
                String cityName = city.getName();
                //更新UI界面上的城市
                tvChooseCity.setText(cityName);
            }

            @Override
            public void onCancel() {
                super.onCancel();
            }
        });
        //弹出城市选择器
        mPicker.showCityPicker();
    }
}
