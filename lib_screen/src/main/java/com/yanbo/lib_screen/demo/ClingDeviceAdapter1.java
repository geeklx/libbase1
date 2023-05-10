package com.yanbo.lib_screen.demo;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yanbo.lib_screen.R;
import com.yanbo.lib_screen.entity.ClingDevice;
import com.yanbo.lib_screen.listener.ItemClickListener;

import java.util.List;

/**
 * 描述：
 *
 * @author Yanbo
 * @date 2018/11/6
 */
public class ClingDeviceAdapter1 extends BaseQuickAdapter<ClingDevice, BaseViewHolder> {
    private ItemClickListener clickListener;

    public void setItemClickListener(ItemClickListener listener) {
        this.clickListener = listener;
    }

    public ClingDeviceAdapter1(@Nullable List<ClingDevice> clingDevice) {
        super(R.layout.item_common_layout, clingDevice);
    }

    @Override
    protected void convert(BaseViewHolder helper, ClingDevice item) {
        TextView tvName = helper.getView(R.id.text_name);
        ImageView iconView = helper.getView(R.id.img_icon);
        tvName.setText(item.getDevice().getDetails().getFriendlyName());
    }
}
