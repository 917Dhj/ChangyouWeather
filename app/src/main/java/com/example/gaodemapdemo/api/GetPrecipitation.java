package com.example.gaodemapdemo.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.gaodemapdemo.LivingAdviceActivity;
import com.example.gaodemapdemo.MainActivity;
import com.example.gaodemapdemo.bean.LivingAdviceBean;
import com.example.gaodemapdemo.bean.PrecipitationBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetPrecipitation {
    private MainActivity mainActivity;

    public GetPrecipitation(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public interface precipitationCallback {
        void onSuccess(String summary); // 成功则返回降水预报的总结
        void onFailure(String errorMessage); // 失败则输出错误提示
    }

    // 通过API获取今日降雨预报
    public void getPrecipitation(String location, precipitationCallback callback) {
        //使用Get异步请求
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                //拼接访问地址
                .url("https://devapi.qweather.com/v7/minutely/5m?location=" + location + "&key=54f045367393423788e7513cd2f8184e")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("getPrecipitation", "获取降水预报失败！");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) { // 回调的方法执行在子线程
                    Log.d("getPrecipitation", "获取降水预报成功了");
                    Log.d("getPrecipitation","response.code()=="+response.code());
                    // 获取的json数据保存在responseBody中
                    String responseBody = response.body().string();
                    Log.d("getPrecipitation","response.body().string()=="+responseBody);
                    // 构建一个Gson对象
                    Gson gson = new Gson();
                    // 使用 Gson 将 JSON 转换成 PrecipitationBean 对象
                    PrecipitationBean precipitationBean = gson.fromJson(responseBody, PrecipitationBean.class);
                    // 通过getter方法获取降水预报总结summary
                    String summary = precipitationBean.getSummary();
                    // 在主线程中执行回调方法，成功则返回summary
                    mainActivity.runOnUiThread(()->callback.onSuccess(summary));
                } else {
                    mainActivity.runOnUiThread(()->callback.onFailure("Respond not successful"));
                }
            }
        });
    }
}
