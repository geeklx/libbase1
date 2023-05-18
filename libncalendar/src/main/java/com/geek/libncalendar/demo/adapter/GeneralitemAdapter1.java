package com.geek.libncalendar.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.geek.libncalendar.R;

import java.util.List;


public class GeneralitemAdapter1 extends RecyclerView.Adapter<GeneralitemAdapter1.ImageHolder> {
    private Context mContext;
    private List<Generalibean> mImageList;

    public GeneralitemAdapter1(List<Generalibean> imageList) {
        mImageList = imageList == null ? mImageList : imageList;
    }

    public void setNewData(List<Generalibean> imageList){
        mImageList = imageList == null ? mImageList : imageList;
        notifyDataSetChanged();
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ImageHolder(LayoutInflater.from(mContext).inflate(R.layout.item_general, parent, false));
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        Generalibean item = mImageList.get(position);
        if (item.getType().equals("1")) {
            holder.iv_icon.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_item));
        } else {
            holder.iv_icon.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_item1));
        }
        holder.tvName.setText(item.getTitle());
        holder.tv_time.setText(item.getTime());
        holder.tv_cjr.setText(item.getCjren());
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    class ImageHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tv_time;
        private TextView tv_cjr;
        private ImageView iv_icon;

        ImageHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_data);
            tv_time = view.findViewById(R.id.tv_time);
            tv_cjr = view.findViewById(R.id.tv_cjr);
            iv_icon = view.findViewById(R.id.iv_icon);
        }
    }
}




