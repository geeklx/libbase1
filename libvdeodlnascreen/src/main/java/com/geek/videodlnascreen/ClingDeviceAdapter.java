package com.geek.videodlnascreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geek.libscreen.entity.ClingDevice;
import com.yanbo.lib_screen.listener.ItemClickListener;
import com.yanbo.lib_screen.manager.DeviceManager;

import java.util.List;

/**
 * 描述：
 *
 * @author Yanbo
 * @date 2018/11/6
 */
public class ClingDeviceAdapter extends RecyclerView.Adapter<ClingDeviceAdapter.ClingHolder> {

    private LayoutInflater layoutInflater;
    private List<ClingDevice> clingDevices;
    private com.yanbo.lib_screen.listener.ItemClickListener clickListener;

    public ClingDeviceAdapter(Context context ) {
        super();
        layoutInflater = LayoutInflater.from(context);
    }

    public void setNewdata(List<ClingDevice> clingDevices){
        this.clingDevices = clingDevices;
    }

    @NonNull
    @Override
    public ClingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_common_layout, parent, false);
        return new ClingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClingHolder holder, final int position) {
        final ClingDevice device = clingDevices.get(position);
        holder.nameView.setText(device.getDevice().getDetails().getFriendlyName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemAction(position, device);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return clingDevices.size();
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.clickListener = listener;
    }

    static class ClingHolder extends RecyclerView.ViewHolder {
        TextView nameView;

        public ClingHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.text_name);
        }
    }
}