package com.tool;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tool.adapter.ExternalDeviceAdapter;
import com.tool.base.BaseActivity;
import com.tool.lib.entity.ExternalDevice;
import com.tool.lib.manager.DetectionManager;

import java.util.List;

/**
 * 外接设备检测页
 */
public class ExternalActivity extends BaseActivity {
    private RecyclerView rvExternalDevice;
    private ExternalDeviceAdapter adapter;

    private AppCompatTextView tvNoneExternal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_devices);

        findViewById(R.id.btn_white_back).setOnClickListener(v ->
                finish());

        tvNoneExternal = findViewById(R.id.tv_none_external);
        rvExternalDevice = findViewById(R.id.rv_external_devices);
        adapter = new ExternalDeviceAdapter(ExternalActivity.this);
        rvExternalDevice.setLayoutManager(new LinearLayoutManager(ExternalActivity.this));
        rvExternalDevice.setAdapter(adapter);

        //获取检测到的外接设备
        List<ExternalDevice> connectedUsbDeviceList = DetectionManager.defaultManager().findExternalDevices(ExternalActivity.this);
        updateUI(connectedUsbDeviceList);

        if (!DetectionManager.defaultManager().isBroadcastRegistered()) {
            DetectionManager.defaultManager().registerDevicesChangeBroadcast(ExternalActivity.this);
        }

        //外设连接状态改变时，广播接收已连接外设
        DetectionManager.defaultManager().obtainExternal(devices -> {
            Log.i("devices:", devices.size() + "");
            updateUI(devices);
        });
    }

    //更新数据
    private void updateUI(List<ExternalDevice> devices) {
        if (devices == null || devices.size() == 0) {
            tvNoneExternal.setVisibility(View.VISIBLE);
            rvExternalDevice.setVisibility(View.GONE);
        } else {
            rvExternalDevice.setVisibility(View.VISIBLE);
            tvNoneExternal.setVisibility(View.GONE);
            //先清除再添加
            adapter.deviceList.clear();
            adapter.deviceList.addAll(devices);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (DetectionManager.defaultManager().isBroadcastRegistered()) {
            DetectionManager.defaultManager().unRegisterDevicesChangeBroadcast(ExternalActivity.this);
        }
    }
}
