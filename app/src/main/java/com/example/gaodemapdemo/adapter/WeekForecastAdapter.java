package com.example.gaodemapdemo.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.QuickViewHolder;
import com.example.gaodemapdemo.R;
import com.example.gaodemapdemo.bean.WeekForecastBean;

import java.util.List;

public class WeekForecastAdapter extends BaseQuickAdapter<WeekForecastBean, QuickViewHolder> {
    public WeekForecastAdapter(@NonNull List<WeekForecastBean> items) {
        super(items);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder quickViewHolder, int i, @Nullable WeekForecastBean weekForecastBean) {
        quickViewHolder.setText(R.id.tv_date, weekForecastBean.getDate());
        quickViewHolder.setImageResource(R.id.iv_weather_status, weekForecastBean.getIcon());
        quickViewHolder.setText(R.id.tv_temp_max, weekForecastBean.getTempMax());
        quickViewHolder.setText(R.id.tv_temp_min, weekForecastBean.getTempMin());
    }

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        return new QuickViewHolder(R.layout.item_seven_day, viewGroup);
    }
}
