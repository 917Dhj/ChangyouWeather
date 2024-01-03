package com.example.gaodemapdemo.model;

import android.util.Log;

import com.example.gaodemapdemo.R;

public class MatchWeatherIcon {
    //这个类用来更新天气预报中的天气图标

    public int matchWeatherIcon(String dayWeather) {
        int icon = 0;
        if (!dayWeather.isEmpty()) {
            switch (dayWeather) {
                case "晴":
                    icon = R.drawable.ic_sun;
                    break;
                case "少云":
                    icon = R.drawable.ic_less_cloud;
                    break;
                case "晴间多云":
                    icon = R.drawable.ic_cloudy_daylight;
                    break;
                case "多云":
                    icon = R.drawable.ic_cloudy;
                    break;
                case "阴":
                    icon = R.drawable.ic_overcast;
                    break;
                case "有风":
                    icon = R.drawable.windy;
                    break;
                case "平静":
                    icon = R.drawable.ic_calm;
                    break;
                case "微风":
                    icon = R.drawable.ic_little_wind;
                    break;
                case "和风":
                    icon = R.drawable.ic_wind_power;
                    break;
                case "清风":
                    icon = R.drawable.ic_light_wind;
                    break;
                case "强风":
                    icon = R.drawable.ic_strong_wind;
                    break;
                case "疾风":
                    icon = R.drawable.ic_fast_wind;
                    break;
                case "大风":
                    icon = R.drawable.ic_big_wind;
                    break;
                case "烈风":
                    icon = R.drawable.ic_bad_wind;
                    break;
                case "风暴":
                    icon = R.drawable.ic_windstorm;
                    break;
                case "狂爆风":
                    icon = R.drawable.ic_crazy_wind;
                    break;
                case "飓风":
                    icon = R.drawable.ic_storm;
                    break;
                case "热带风暴":
                    icon = R.drawable.ic_typhoon;
                    break;
                case "霾":
                    icon = R.drawable.ic_mai;
                    break;
                case "中度霾":
                    icon = R.drawable.ic_middle_mai;
                    break;
                case "重度霾":
                    icon = R.drawable.ic_heavy_mai;
                    break;
                case "严重霾":
                    icon = R.drawable.ic_serious_mai;
                    break;
                case "阵雨":
                    icon = R.drawable.ic_showers_daylight;
                    break;
                case "雷阵雨":
                    icon = R.drawable.ic_thunderstorm;
                    break;
                case "雷阵雨并伴有冰雹":
                    icon = R.drawable.ic_thunderstorm_ice;
                    break;
                case "小雨":
                    icon = R.drawable.ic_light_rain;
                    break;
                case "中雨":
                    icon = R.drawable.ic_moderate_rain;
                    break;
                case "大雨":
                    icon = R.drawable.ic_heavy_rain;
                    break;
                case "暴雨":
                    icon = R.drawable.ic_torrential_rain;
                    break;
                case "大暴雨":
                    icon = R.drawable.ic_downpour;
                    break;
                case "特大暴雨":
                    icon = R.drawable.ic_heavy_downpour;
                    break;
                case "强阵雨":
                    icon = R.drawable.ic_strong_shower;
                    break;
                case "强雷阵雨":
                    icon = R.drawable.ic_strong_thundershower;
                    break;
                case "极端降雨":
                    icon = R.drawable.ic_extreme_downpour;
                    break;
                case "毛毛雨":
                    icon = R.drawable.ic_rain_drops;
                    break;
                case "细雨":
                    icon = R.drawable.ic_rain_drops;
                    break;
                case "雨":
                    icon = R.drawable.ic_rain;
                    break;
                case "小到中雨":
                    icon = R.drawable.ic_moderate_rain;
                    break;
                case "中到大雨":
                    icon = R.drawable.ic_heavy_rain;
                    break;
                case "大到暴雨":
                    icon = R.drawable.ic_torrential_rain;
                    break;
                case "暴雨到大暴雨":
                    icon = R.drawable.ic_downpour;
                    break;
                case "大暴雨到特大暴雨":
                    icon = R.drawable.ic_heavy_downpour;
                    break;
                case "雨雪天气":
                    icon = R.drawable.ic_rain_snow;
                    break;
                case "雨夹雪":
                    icon = R.drawable.ic_shower_and_snow;
                    break;
                case "阵雨夹雪":
                    icon = R.drawable.ic_rain_and_snow;
                    break;
                case "冻雨":
                    icon = R.drawable.ic_frozen_rain;
                    break;
                case "雪":
                    icon = R.drawable.ic_light_snow;
                    break;
                case "阵雪":
                    icon = R.drawable.ic_snow_showers_daylight;
                    break;
                case "小雪":
                    icon = R.drawable.ic_light_snow;
                    break;
                case "中雪":
                    icon = R.drawable.ic_moderate_snow;
                    break;
                case "大雪":
                    icon = R.drawable.ic_heavy_snow;
                    break;
                case "暴雪":
                    icon = R.drawable.ic_snow_storm;
                    break;
                case "小到中雪":
                    icon = R.drawable.ic_moderate_snow;
                    break;
                case "中到大雪":
                    icon = R.drawable.ic_heavy_snow;
                    break;
                case "大到暴雪":
                    icon = R.drawable.ic_snow_storm;
                    break;
                case "浮尘":
                    icon = R.drawable.ic_floating_dust;
                    break;
                case "扬沙":
                    icon = R.drawable.ic_floating_sand;
                    break;
                case "沙尘暴":
                    icon = R.drawable.ic_sandstorm;
                    break;
                case "强沙尘暴":
                    icon = R.drawable.ic_strong_sandstorm;
                    break;
                case "龙卷风":
                    icon = R.drawable.ic_tornado;
                    break;
                case "雾":
                    icon = R.drawable.ic_fog;
                    break;
                case "浓雾":
                    icon = R.drawable.ic_dense_fog;
                    break;
                case "强浓雾":
                    icon = R.drawable.ic_strong_dense_fog;
                    break;
                case "薄雾":
                    icon = R.drawable.ic_light_fog;
                    break;
                case "大雾":
                    icon = R.drawable.ic_heavy_fog;
                    break;
                case "特强浓雾":
                    icon = R.drawable.ic_extreme_dense_fog;
                    break;
                case "热":
                    icon = R.drawable.ic_hot;
                    break;
                case "冷":
                    icon = R.drawable.ic_snow;
                    break;
                default:
                    Log.d("setWeatherIcon", "没有对应的天气图标");
                    break;
            }
        } else {
            Log.d("matchDayWeatherIcon", "dayWeather为空");
        }
        return icon;
    }
}
