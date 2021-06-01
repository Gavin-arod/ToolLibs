package com.tool.lib.manager;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;

import com.tool.lib.impl.ExternalDevicesStatusChangeListener;
import com.tool.lib.entity.ExternalDevice;
import com.tool.lib.impl.CurrentNetSpeedListener;
import com.tool.lib.receiver.ExternalDevicesBroadcastReceiver;
import com.tool.lib.utils.ExternalUtil;

import java.util.List;

/**
 * 网络状态、外设检测管理类
 */
public class DetectionManager {
    private static DetectionManager manager;

    private static NetSpeedDetectionProgress speedDetectionProgress;
    private static ExternalDevicesBroadcastReceiver externalDevicesReceiver;

    public DetectionManager() {
        super();
    }

    public static DetectionManager defaultManager() {
        if (manager == null) {
            synchronized (DetectionManager.class) {
                if (manager == null) {
                    manager = new DetectionManager();
                }
            }
        }
        return manager;
    }

    //开始网速检测
    public void startDetectNetSpeed(CurrentNetSpeedListener listener) {
        speedDetectionProgress = new NetSpeedDetectionProgress();
        speedDetectionProgress.setCurrentNetSpeedListener(listener);
        speedDetectionProgress.run();
    }

    //取消检测
    public void cancelDetectNetSpeed() {
        if (speedDetectionProgress != null) {
            speedDetectionProgress.cancel();
        }
    }

    //网速检测正在执行
    public boolean netDetectionIsRunning() {
        if (speedDetectionProgress != null) {
            return speedDetectionProgress.isRunning();
        } else {
            return false;
        }
    }

    /**
     * 判断是否有外接设备
     */
    public boolean hasExternal(Context context) {
        return ExternalUtil.hasExternalDevice(context);
    }

    /**
     * 检测外接设备
     *
     * @return devices
     */
    public List<ExternalDevice> findExternalDevices(Context context) {
        return ExternalUtil.getALLConnectedExternalDevices(context);
    }

    /**
     * 注册广播接收外设变动情况
     */
    public void registerDevicesChangeBroadcast(Context context) {
        if (externalDevicesReceiver == null) {
            externalDevicesReceiver = new ExternalDevicesBroadcastReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        context.registerReceiver(externalDevicesReceiver, filter);
    }

    /**
     * 获取外设检测广播注册情况
     */
    public boolean isBroadcastRegistered() {
        return externalDevicesReceiver != null;
    }

    /**
     * 获取变动后的外接设备
     */
    public void obtainExternal(ExternalDevicesStatusChangeListener listener) {
        if (externalDevicesReceiver != null) {
            externalDevicesReceiver.setDevicesStatusChangeListener(listener);
        }
    }

    /**
     * 取消广播注册
     */
    public void unRegisterDevicesChangeBroadcast(Context context) {
        if (externalDevicesReceiver != null) {
            context.unregisterReceiver(externalDevicesReceiver);
            externalDevicesReceiver = null;
        }
    }

}
