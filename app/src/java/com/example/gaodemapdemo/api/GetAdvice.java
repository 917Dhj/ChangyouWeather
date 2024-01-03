package com.example.gaodemapdemo.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.gaodemapdemo.LivingAdviceActivity;
import com.example.gaodemapdemo.bean.LivingAdviceBean;
import com.example.gaodemapdemo.bean.TodayWeatherBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetAdvice {
    // 这个类用来通过API获取天气数据
    private LivingAdviceActivity livingAdviceActivity;

    public GetAdvice(LivingAdviceActivity livingAdviceActivity) {
        this.livingAdviceActivity = livingAdviceActivity;
    }

    public interface adviceCallback {
        void onSuccess(List<LivingAdviceBean.DailyDTO> dailyDTOList); // 成功则返回生活建议信息
        void onFailure(String errorMessage); // 失败则输出错误提示
    }

    // 通过API获取今日生活建议
    public void getAdvice(String location, adviceCallback callback) {
        //使用Get异步请求
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                //拼接访问地址
                .url("https://devapi.qweather.com/v7/indices/1d?type=0&location=" + location + "&key=54f045367393423788e7513cd2f8184e")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("getLivingAdvice", "获取生活建议失败！");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) { // 回调的方法执行在子线程
                    Log.d("getAdvice", "获取今日建议数据成功了");
                    Log.d("getAdvice","response.code()=="+response.code());
                    String responseBody = response.body().string();
                    Log.d("getAdvice","response.body().string()=="+responseBody);
                    // 构建一个Gson对象
                    Gson gson = new Gson();
                    // 使用 Gson 将 JSON 转换成 LivingAdviceBean 对象
                    LivingAdviceBean livingAdviceBean = gson.fromJson(responseBody, LivingAdviceBean.class);
                    // 获取实时生活建议，保存在dailyDTOList中，再通过回调方法返回给需要使用生活建议的地方
                    List<LivingAdviceBean.DailyDTO> dailyDTOList = livingAdviceBean.getDaily();
                    // 在主线程中执行回调方法
                    livingAdviceActivity.runOnUiThread(()->callback.onSuccess(dailyDTOList));
                } else {
                    livingAdviceActivity.runOnUiThread(()->callback.onFailure("Respond not successful"));
                }
            }
        });
    }
}
