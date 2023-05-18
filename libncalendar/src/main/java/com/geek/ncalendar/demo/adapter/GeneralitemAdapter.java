package com.geek.ncalendar.demo.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.geek.ncalendar.R;

import java.util.List;


/**
 * Created by necer on 2017/6/7.
 */


public class GeneralitemAdapter extends BaseQuickAdapter<Generalibean, BaseViewHolder> {


    public GeneralitemAdapter() {
        super(R.layout.item_general);
    }

    @Override
    protected void convert(BaseViewHolder helper, Generalibean item) {
        TextView tvName = helper.getView(R.id.tv_data);
        TextView tv_time = helper.getView(R.id.tv_time);
        TextView tv_cjr = helper.getView(R.id.tv_cjr);
        ImageView iv_icon = helper.getView(R.id.iv_icon);
        if (item.getType().equals("1")) {
            iv_icon.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_item));
        } else {
            iv_icon.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_item1));
        }
        tvName.setText(item.getTitle());
        tv_time.setText(item.getTime());
        tv_cjr.setText(item.getCjren());
    }
}




