package com.example.gaodemapdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gaodemapdemo.api.GetAdvice;
import com.example.gaodemapdemo.bean.LivingAdviceBean;

import java.util.ArrayList;
import java.util.List;

public class LivingAdviceActivity extends AppCompatActivity {
    private ImageView ivBackPress;
    private TextView tvSuitable;
    private TextView tvUnSuited;
    private TextView tvComfort;
    private TextView tvTips;

    @SuppressLint({"MissingInflatedId", "UnspecifiedRegisterReceiverFlag"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living_advice);
        initView();

        //从intent获取MainActivity传来的location
        String location = getIntent().getStringExtra("location");

        // 用location来获取今日生活建议
        GetAdvice getAdvice = new GetAdvice(this);
        getAdvice.getAdvice(location, new GetAdvice.adviceCallback() {
            @Override
            public void onSuccess(List<LivingAdviceBean.DailyDTO> dailyDTOList) {
                Log.d("getAdvice", "获取生活建议成功！");
                updateAdvice(dailyDTOList);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("getAdvice", "获取生活建议失败！");
            }
        });
    }

    private void initView() {
        ivBackPress = findViewById(R.id.iv_return);
        tvSuitable = findViewById(R.id.tv_suitable);
        tvUnSuited = findViewById(R.id.tv_unsuited);
        tvComfort = findViewById(R.id.tv_comf);
        tvTips = findViewById(R.id.tv_context);
        ivBackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //模拟返回键的行为
                onBackPressed();
            }
        });
    }

    // 每一个adviceType代表一种生活建议指数
    // 1-运动指数   2-洗车指数  3-穿衣指数  4-钓鱼指数  5-紫外线指数   6-旅游指数    7-花粉过敏指数  8-舒适度指数
    // 9-感冒指数   10-空气污染扩散条件指数   11-空调开启指数   12-太阳镜指数    13-化妆指数     14-晾晒指数
    // 15-交通指数  16-防晒指数
    private void updateAdvice(List<LivingAdviceBean.DailyDTO> dailyDTOList) {
        LivingAdviceBean.DailyDTO adviceType1 = dailyDTOList.get(0);
        LivingAdviceBean.DailyDTO adviceType2 = dailyDTOList.get(1);
        LivingAdviceBean.DailyDTO adviceType3 = dailyDTOList.get(2);
        LivingAdviceBean.DailyDTO adviceType4 = dailyDTOList.get(3);
        LivingAdviceBean.DailyDTO adviceType5 = dailyDTOList.get(4);
        LivingAdviceBean.DailyDTO adviceType6 = dailyDTOList.get(5);
        LivingAdviceBean.DailyDTO adviceType7 = dailyDTOList.get(6);
        LivingAdviceBean.DailyDTO adviceType8 = dailyDTOList.get(7);
        LivingAdviceBean.DailyDTO adviceType9 = dailyDTOList.get(8);
        LivingAdviceBean.DailyDTO adviceType10 = dailyDTOList.get(9);
        LivingAdviceBean.DailyDTO adviceType11 = dailyDTOList.get(10);
        LivingAdviceBean.DailyDTO adviceType12 = dailyDTOList.get(11);
        LivingAdviceBean.DailyDTO adviceType13 = dailyDTOList.get(12);
        LivingAdviceBean.DailyDTO adviceType14 = dailyDTOList.get(13);
        LivingAdviceBean.DailyDTO adviceType15 = dailyDTOList.get(14);
        LivingAdviceBean.DailyDTO adviceType16 = dailyDTOList.get(15);

        List<String> todoList = new ArrayList<>(); // 适宜列表
        List<String> notDoList = new ArrayList<>(); // 不宜列表
        List<String> tipsList = new ArrayList<>(); // 小贴士列表
        // 运动
        if (Integer.parseInt(adviceType1.getLevel()) < 3 && !adviceType1.getLevel().isEmpty()) {
            todoList.add("运动");
        } else {
            notDoList.add("运动");
        }
        // 洗车
        if (Integer.parseInt(adviceType2.getLevel()) < 3 && !adviceType2.getLevel().isEmpty()) {
            todoList.add("洗车");
        } else {
            notDoList.add("洗车");
        }
        // 钓鱼
        if (Integer.parseInt(adviceType4.getLevel()) < 3 && !adviceType4.getLevel().isEmpty()) {
            todoList.add("钓鱼");
        } else {
            notDoList.add("钓鱼");
        }
        // 旅游
        if (Integer.parseInt(adviceType6.getLevel()) < 4 && !adviceType6.getLevel().isEmpty()) {
            todoList.add("旅游");
        } else {
            notDoList.add("旅游");
        }
        // 空调
        if (Integer.parseInt(adviceType11.getLevel()) == 1 && !adviceType11.getLevel().isEmpty()) {
            todoList.add("长时间开空调");
        } else if (Integer.parseInt(adviceType11.getLevel()) == 2 && !adviceType11.getLevel().isEmpty()){
            todoList.add("部分时间开空调");
        } else if (Integer.parseInt(adviceType11.getLevel()) == 4 && !adviceType11.getLevel().isEmpty()) {
            todoList.add("开启制暖空调");
        } else {
            notDoList.add("开空调");
        }
        // 太阳镜
        if (Integer.parseInt(adviceType12.getLevel()) > 2 && !adviceType12.getLevel().isEmpty()) {
            todoList.add("戴太阳镜");
        }
        // 晾晒
        if (Integer.parseInt(adviceType14.getLevel()) < 4 && !adviceType14.getLevel().isEmpty()) {
            todoList.add("晾晒");
        } else {
            notDoList.add("晾晒");
        }
        // 交通
        if (Integer.parseInt(adviceType15.getLevel()) < 4 && !adviceType15.getLevel().isEmpty()) {
            todoList.add("参与交通");
        } else {
            notDoList.add("参与交通");
        }
        // 舒适度
        if (Integer.parseInt(adviceType8.getLevel()) == 1 && !adviceType8.getLevel().isEmpty()) {
            tvComfort.append("很舒适！");
        } else if (Integer.parseInt(adviceType8.getLevel()) == 2 && !adviceType8.getLevel().isEmpty()) {
            tvComfort.append("较舒适！");
        } else if (Integer.parseInt(adviceType8.getLevel()) == 3 && !adviceType8.getLevel().isEmpty()) {
            tvComfort.append("一般");
        } else if (Integer.parseInt(adviceType8.getLevel()) == 4 && !adviceType8.getLevel().isEmpty()) {
            tvComfort.append("较不舒适");
        } else if (Integer.parseInt(adviceType8.getLevel()) == 5 && !adviceType8.getLevel().isEmpty()) {
            tvComfort.append("不舒适");
        } else if (Integer.parseInt(adviceType8.getLevel()) == 6 && !adviceType8.getLevel().isEmpty()) {
            tvComfort.append("很不舒适");
        } else if (Integer.parseInt(adviceType8.getLevel()) == 7 && !adviceType8.getLevel().isEmpty()) {
            tvComfort.append("非常不舒适！");
        }
        // 穿衣
        if (!adviceType3.getText().isEmpty()) { // 判断一下是不是空的
            tipsList.add("·"+adviceType3.getText());
        } else {
            Log.d("clothing", "没有穿衣tips");
        }
        // 化妆
        if (Integer.parseInt(adviceType13.getLevel()) == 1 && !adviceType13.getLevel().isEmpty()) {
            tipsList.add("·化妆注意保湿。");
        } else if (Integer.parseInt(adviceType13.getLevel()) == 2 && !adviceType13.getLevel().isEmpty()){
            tipsList.add("·化妆注意保湿防晒。");
        } else if (Integer.parseInt(adviceType13.getLevel()) == 3 && !adviceType13.getLevel().isEmpty()) {
            tipsList.add("·化妆注意去油防晒。");
        } else if (Integer.parseInt(adviceType13.getLevel()) == 4 && !adviceType13.getLevel().isEmpty()) {
            tipsList.add("·化妆注意防脱水防晒。");
        } else if (Integer.parseInt(adviceType13.getLevel()) == 5 && !adviceType13.getLevel().isEmpty()) {
            tipsList.add("·化妆注意去油。");
        } else if (Integer.parseInt(adviceType13.getLevel()) == 6 && !adviceType13.getLevel().isEmpty()) {
            tipsList.add("·化妆注意防脱水。");
        } else if (Integer.parseInt(adviceType13.getLevel()) == 7 && !adviceType13.getLevel().isEmpty()) {
            tipsList.add("·化妆注意防晒。");
        } else if (Integer.parseInt(adviceType13.getLevel()) == 8 && !adviceType13.getLevel().isEmpty()) {
            tipsList.add("·化妆注意滋润保湿。");
        }
        // 紫外线
        if (Integer.parseInt(adviceType5.getLevel()) == 1 && !adviceType5.getLevel().isEmpty()) {
            tipsList.add("·今日紫外线很弱，无需特别防护。");
        } else if (Integer.parseInt(adviceType5.getLevel()) == 2 && !adviceType5.getLevel().isEmpty()) {
            tipsList.add("·今日紫外线较弱，无需特别防护。");
        } else if (Integer.parseInt(adviceType5.getLevel()) == 3 && !adviceType5.getLevel().isEmpty()) {
            tipsList.add("·今日紫外线中等，可适当防护。");
        } else if (Integer.parseInt(adviceType5.getLevel()) == 4 && !adviceType5.getLevel().isEmpty()) {
            tipsList.add("·今日紫外线较强，注意防护！");
        } else if (Integer.parseInt(adviceType5.getLevel()) == 5 && !adviceType5.getLevel().isEmpty()) {
            tipsList.add("·今日紫外线很强，注意防护！");
        }
        // 过敏
        if (!adviceType7.getText().isEmpty()) { // 判断一下是不是空的
            tipsList.add("·"+adviceType7.getText());
        } else {
            Log.d("guoming", "没有过敏tips！");
        }
        // 感冒
        if (!adviceType9.getText().isEmpty()) {
            tipsList.add("·"+adviceType9.getText());
        } else {
            Log.d("cold", "没有感冒tips！");
        }
        // 空气污染扩散条件
        if (!adviceType10.getText().isEmpty()) {
            tipsList.add("·"+adviceType10.getText());
        } else {
            Log.d("air", "没有空气污染tips！");
        }
        // 防晒
        if (!adviceType16.getText().isEmpty()) {
            tipsList.add("·"+adviceType16.getText());
        } else {
            Log.d("sunproof", "没有防晒tips！");
        }

        // 将todoList中的内容放进适宜做的事情组件中
        if (!todoList.isEmpty()) { // 如果todolist不是空的
            for (int i=0;i < todoList.size();i++) { // 就把list中的事情一个个放进textview里
                tvSuitable.append(todoList.get(i)+"  ");
            }
        } else {
            tvSuitable.setText("今天天气不太好，适合躺平！");
        }
        // 将notDOList中的内容放进不适合做的事情组件中
        if (!notDoList.isEmpty()) { // 如果notDoList不是空的
            for (int i=0;i < notDoList.size();i++) { // 就把list中的事情一个个放进textview里
                tvUnSuited.append(notDoList.get(i)+"  ");
            }
        } else {
            tvUnSuited.setText("今天是个好天气！\n快去做自己想做的事情吧！");
        }
        // 将tipsList中的内容放进小贴士的内容中
        if (!tipsList.isEmpty()) { // 如果tipsList不是空的
            for (int i=0;i < tipsList.size();i++) { // 就把list中的事情一个个放进textview里
                tvTips.append((i+1)+tipsList.get(i)+"\n");
            }
        } else {
            tvTips.setText("享受生活吧！\n");
        }
    }
}