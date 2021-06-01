package com.tool.lib.receiver;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.widget.Toast;

import com.tool.lib.impl.ExternalDevicesStatusChangeListener;
import com.tool.lib.entity.ExternalDevice;
import com.tool.lib.manager.DetectionManager;

import java.util.List;

/**
 * 广播接收：所有外接设备状态监听（已连接or未连接）
 */
public class ExternalDevicesBroadcastReceiver extends BroadcastReceiver {
    private ExternalDevicesStatusChangeListener devicesStatusChangeListener;

    public void setDevicesStatusChangeListener(ExternalDevicesStatusChangeListener devicesStatusChangeListener) {
        this.devicesStatusChangeListener = devicesStatusChangeListener;
    }

    public ExternalDevicesBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case UsbManager.ACTION_USB_DEVICE_ATTACHED:
                    Toast.makeText(context, "USB设备已连接", Toast.LENGTH_SHORT).show();
                    setExternal(context);
                    break;
                case UsbManager.ACTION_USB_DEVICE_DETACHED:
                    Toast.makeText(context, "USB设备已断开", Toast.LENGTH_SHORT).show();
                    setExternal(context);
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    Toast.makeText(context, "蓝牙设备已断开", Toast.LENGTH_SHORT).show();
                    setExternal(context);
                    break;
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    Toast.makeText(context, "蓝牙设备已连接", Toast.LENGTH_SHORT).show();
                    setExternal(context);
                    break;
            }
        }
    }

    private void setExternal(Context context) {
        if (devicesStatusChangeListener == null) {
            return;
        }
        //延迟获取设备信息，避免收到广播时设备信息为空
        new Handler().postDelayed(() -> {
            List<ExternalDevice> connectedDevicesList = DetectionManager.defaultManager().findExternalDevices(context);
            devicesStatusChangeListener.onChange(connectedDevicesList);
        }, 1000);
    }
}
