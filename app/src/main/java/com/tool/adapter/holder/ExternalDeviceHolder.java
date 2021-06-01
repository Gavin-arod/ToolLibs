package com.tool.adapter.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.tool.R;

/**
 * 外接设备ViewHolder
 */
public class ExternalDeviceHolder extends RecyclerView.ViewHolder {
    private AppCompatImageView deviceIconView;
    private AppCompatTextView deviceNameView;
    private View dividerLineView;


    public AppCompatImageView getDeviceIconView() {
        return deviceIconView;
    }

    public void setDeviceIconView(AppCompatImageView deviceIconView) {
        this.deviceIconView = deviceIconView;
    }

    public AppCompatTextView getDeviceNameView() {
        return deviceNameView;
    }

    public void setDeviceNameView(AppCompatTextView deviceNameView) {
        this.deviceNameView = deviceNameView;
    }

    public View getDividerLineView() {
        return dividerLineView;
    }

    public void setDividerLineView(View dividerLineView) {
        this.dividerLineView = dividerLineView;
    }

    public ExternalDeviceHolder(@NonNull View itemView) {
        super(itemView);
        setDeviceIconView(itemView.findViewById(R.id.iv_device_icon));
        setDeviceNameView(itemView.findViewById(R.id.tv_device_name));
        setDividerLineView(itemView.findViewById(R.id.top_divider_line));
    }
}
