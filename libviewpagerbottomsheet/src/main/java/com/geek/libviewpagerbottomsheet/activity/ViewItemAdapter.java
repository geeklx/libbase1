package com.geek.libviewpagerbottomsheet.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.geek.libviewpagerbottomsheet.R;

import java.util.List;

/**
 * @author fosung
 */

class ViewItemAdapter extends RecyclerView.Adapter<ViewItemAdapter.ImageHolder> {
    private Context mContext;
    private List<String> mImageList;

    ViewItemAdapter(List<String> imageList) {
        mImageList = imageList == null ? mImageList : imageList;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ImageHolder(LayoutInflater.from(mContext).inflate(R.layout.item_caijicard, parent, false));
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        String image = mImageList.get(position);
        holder.originArg.setText(image);
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    class ImageHolder extends RecyclerView.ViewHolder {
        private TextView originArg;
        private TextView thumbArg;
        private ImageView image;

        ImageHolder(View view) {
            super(view);
            originArg = view.findViewById(R.id.tv_name);
        }
    }
}
