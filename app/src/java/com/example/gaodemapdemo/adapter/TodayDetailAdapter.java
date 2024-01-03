package com.example.gaodemapdemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.QuickViewHolder;
import com.example.gaodemapdemo.R;
import com.example.gaodemapdemo.bean.TodayDetailBean;
import com.example.gaodemapdemo.bean.TodayWeatherBean;

import java.util.List;

public class TodayDetailAdapter extends BaseQuickAdapter<TodayDetailBean, QuickViewHolder> {
    // 自定义一个Adapter，继承第三方库BaseQuickAdapter，为RecyclerView传入数据

    public TodayDetailAdapter(@NonNull List<TodayDetailBean> items) {
        super(items);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder quickViewHolder, int i, @Nullable TodayDetailBean todayDetailBean) {
        quickViewHolder.setImageResource(R.id.iv_icon, todayDetailBean.getIcon());
        quickViewHolder.setText(R.id.tv_value, todayDetailBean.getValue());
        quickViewHolder.setText(R.id.tv_name, todayDetailBean.getName());
    }

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        return new QuickViewHolder(R.layout.item_today_detail, viewGroup);
    }
}
