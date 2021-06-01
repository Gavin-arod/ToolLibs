package com.tool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tool.R;
import com.tool.adapter.holder.ExternalDeviceHolder;
import com.tool.lib.constants.Constants;
import com.tool.lib.entity.ExternalDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * 外接设备adapter
 */
public class ExternalDeviceAdapter extends RecyclerView.Adapter<ExternalDeviceHolder> {
    private final Context mContext;
    public List<ExternalDevice> deviceList = new ArrayList<>();

    public ExternalDeviceAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ExternalDeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_external_device, parent, false);
        return new ExternalDeviceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExternalDeviceHolder holder, int position) {
        if (position == 0) {
            holder.getDividerLineView().setVisibility(View.GONE);
        }

        ExternalDevice device = deviceList.get(position);
        holder.getDeviceNameView().setText(device.getProductName());

        int imageResId = 0;
        if (device.getDeviceType() == Constants.TYPE_DEVICE_JOYSTICK) {
            imageResId = R.mipmap.icon_type_joystick;
        } else if (device.getDeviceType() == Constants.TYPE_DEVICE_KEYBOARD) {
            imageResId = R.mipmap.icon_type_keyboard;
        } else if (device.getDeviceType() == Constants.TYPE_DEVICE_MOUSE) {
            imageResId = R.mipmap.icon_type_mouse;
        }

        holder.getDeviceIconView().setImageResource(imageResId);
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }
}
