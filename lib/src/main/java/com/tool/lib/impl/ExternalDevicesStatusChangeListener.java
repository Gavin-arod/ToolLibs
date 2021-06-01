package com.tool.lib.impl;

import com.tool.lib.entity.ExternalDevice;

import java.util.List;

/**
 * 所有输入设备连接状态监听
 */
public interface ExternalDevicesStatusChangeListener {
    void onChange(List<ExternalDevice> devices);
}
