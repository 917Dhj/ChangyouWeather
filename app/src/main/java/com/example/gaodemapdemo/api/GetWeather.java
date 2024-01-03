package com.example.gaodemapdemo.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.gaodemapdemo.MainActivity;
import com.example.gaodemapdemo.bean.TodayWeatherBean;
import com.example.gaodemapdemo.bean.WeatherForecastBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetWeather {
    // 这个类用来通过API获取天气数据
    private MainActivity mainActivity;

    // 构造函数，需要传入一个MainActivity对象，用来调用runOnUiThread更新UI线程
    public GetWeather(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    // 创建一个回调方法，用于返回获取到的天气信息
    //这里不用函数直接return而是用回调方法的原因是，用OkHttp请求天气信息是在一个异步线程中，就会出现return的信息还没被定义或者初始化的情况，所以不可行。
    public interface todayWeatherCallback {
        void onSuccess(TodayWeatherBean.NowDTO nowDTO); // 成功则返回今日天气信息
        void onFailure(String errorMessage); // 失败则输出错误提示
    }

    public interface weatherForecastCallback {
        void onSuccess(List<WeatherForecastBean.DailyDTO> dailyDTOSList); // 成功则返回未来7日的天气预报
        void onFailure(String errorMessage); // 失败则输出错误提示
    }

    //获取今天的天气数据
    public void getTodayWeather(String location, todayWeatherCallback callback) {
        //使用Get异步请求
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                //拼接访问地址
                .url("https://devapi.qweather.com/v7/weather/now?location=" + location + "&key=54f045367393423788e7513cd2f8184e")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("getTodayWeather", "获取天气数据失败！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) { // 回调的方法执行在子线程
                    Log.d("getTodayWeather", "获取天气数据成功了");
                    Log.d("getTodayWeather","response.code()=="+response.code());
                    String responseBody = response.body().string();
                    Log.d("getTodayWeather","response.body().string()=="+responseBody);
                    // 构建一个Gson对象
                    Gson gson = new Gson();
                    // 使用 Gson 将 JSON 转换成 WeatherBean 对象
                    TodayWeatherBean todayWeatherBean = gson.fromJson(responseBody, TodayWeatherBean.class);
                    // 获取实时天气，保存在nowDTO中，再通过回调方法返回给需要使用天气信息的地方
                    TodayWeatherBean.NowDTO nowDTO = todayWeatherBean.getNow();
                    String temperature = nowDTO.getTemp();
                    Log.d("getTemp", "气温是" + temperature);
                    // 因为回调操作会涉及UI更新，所以用runOnUiThread函数确保回调操作在主线程中进行
                    mainActivity.runOnUiThread(()->callback.onSuccess(nowDTO));
                    }else {
                    mainActivity.runOnUiThread(()->callback.onFailure("Respond not successful"));
                }
            }
        });
    }

    // 根据location获取该地区未来7日的天气预报
    public void getWeatherForecast(String location, weatherForecastCallback callback) {
        //使用Get异步请求
        OkHttpClient okHttpClient = new OkHttpClient();
        Request requestForecast = new Request.Builder()
                .url("https://devapi.qweather.com/v7/weather/7d?location=" + location + "&key=54f045367393423788e7513cd2f8184e")
                .build();
        okHttpClient.newCall(requestForecast).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("getWeatherForecast", "获取天气预报数据失败！");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("onResponse", "获取天气预报成功了");
                    String forecastBody = response.body().string();
                    Log.d("kwwl", "response.body().string()==" + forecastBody);
                    // 构建一个Gson对象
                    Gson gson = new Gson();
                    // 使用 Gson 将 JSON 转换成 WeatherForecastBean 对象
                    WeatherForecastBean weatherForecastBean = gson.fromJson(forecastBody, WeatherForecastBean.class);
                    //根据WeatherForecastBean类的内容来逐步解析weatherForecastBean
                    //dailyDTOSList列表中的每一项都是一个DailyDTO对象，该列表一共有7项，也就是未来7天的预报
                    List<WeatherForecastBean.DailyDTO> dailyDTOSList = weatherForecastBean.getDaily();

                    // 因为回调操作会涉及UI更新，所以用runOnUiThread函数确保回调操作在主线程中进行
                    mainActivity.runOnUiThread(() -> callback.onSuccess(dailyDTOSList));
                }else {
                    mainActivity.runOnUiThread(()->callback.onFailure("Respond not successful"));
                }
            }
        });
    }
}

