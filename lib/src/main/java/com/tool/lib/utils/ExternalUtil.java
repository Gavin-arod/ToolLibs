package com.tool.lib.utils;

import android.content.Context;
import android.hardware.input.InputManager;
import android.view.InputDevice;

import com.tool.lib.constants.Constants;
import com.tool.lib.entity.ExternalDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * 外接设备工具类
 */
public class ExternalUtil {
    /**
     * 获取已连接的所有的输入设备
     */
    public static List<ExternalDevice> getALLConnectedExternalDevices(Context context) {
        List<ExternalDevice> externalDeviceList = new ArrayList<>();
        InputManager inputManager = (InputManager) context.getSystemService(Context.INPUT_SERVICE);
        int[] devices = inputManager.getInputDeviceIds();
        if (devices == null) {
            return null;
        }
        for (int id : devices) {
            InputDevice device = inputManager.getInputDevice(id);
            if (device == null) {
                continue;
            }
            //判断检测到的设备是内建还是外设
            if (!isExternal(device)) {
                continue;
            }

            //判断外设：鼠标、键盘、手柄
            ExternalDevice externalDevice;
            if (isJoystick(device)) {
                //手柄属性：keyboard joystick gamepad
                externalDevice = buildExternalDevice(device, Constants.TYPE_DEVICE_JOYSTICK);
                externalDeviceList.add(externalDevice);
            } else if (isMouse(device)) {
                //鼠标
                //常规的鼠标：InputDevice.SOURCE_MOUSE
                //非常规：属性：( keyboard dpad mouse joystick )
                externalDevice = buildExternalDevice(device, Constants.TYPE_DEVICE_MOUSE);
                externalDeviceList.add(externalDevice);
            } else if (isKeyboard(device)) {
                //键盘属性：keyboard
                //Keyboard Type: alphabetic
                externalDevice = buildExternalDevice(device, Constants.TYPE_DEVICE_KEYBOARD);
                externalDeviceList.add(externalDevice);
            }
        }
        return externalDeviceList;
    }

    /**
     * 判断是手柄
     */
    public static boolean isJoystick(InputDevice device) {
        return (device.getSources() & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK
                && (device.getSources() & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD
                && (device.getSources() & InputDevice.SOURCE_KEYBOARD) == InputDevice.SOURCE_KEYBOARD;
    }

    /**
     * 判断是键盘
     */
    public static boolean isKeyboard(InputDevice device) {
        return device.getSources() == InputDevice.SOURCE_KEYBOARD 
                && device.getKeyboardType() == InputDevice.KEYBOARD_TYPE_ALPHABETIC;
    }

    /**
     * 判断是鼠标
     */
    public static boolean isMouse(InputDevice device) {
        return device.getSources() == InputDevice.SOURCE_MOUSE ||
                ((device.getSources() & InputDevice.SOURCE_MOUSE) == InputDevice.SOURCE_MOUSE
                        && (device.getSources() & InputDevice.SOURCE_DPAD) == InputDevice.SOURCE_DPAD
                        && (device.getSources() & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK
                        && (device.getSources() & InputDevice.SOURCE_KEYBOARD) == InputDevice.SOURCE_KEYBOARD);
    }

    /**
     * 判断设备是外设
     */
    public static boolean isExternal(InputDevice device) {
        String deviceStr = device.toString();
        String resultStr = deviceStr.
                substring(deviceStr.indexOf("Location"))
                .replace("Location", "")
                .replace(":", "")
                .trim();
        return resultStr.startsWith("external");
    }

    /**
     * 是否有外接设备(鼠标、键盘、手柄)
     */
    public static boolean hasExternalDevice(Context context) {
        List<ExternalDevice> list = getALLConnectedExternalDevices(context);
        return list != null && list.size() != 0;
    }

    /**
     * 有手柄连接
     */
    public static boolean hasJoystick(List<ExternalDevice> devices) {
        for (ExternalDevice device : devices) {
            if (device.getDeviceType() == Constants.TYPE_DEVICE_JOYSTICK) {
                return true;
            }
        }
        return false;
    }

    /**
     * 有鼠标连接
     */
    public static boolean hasMouse(List<ExternalDevice> devices) {
        for (ExternalDevice device : devices) {
            if (device.getDeviceType() == Constants.TYPE_DEVICE_MOUSE) {
                return true;
            }
        }
        return false;
    }

    /**
     * 有键盘连接
     */
    public static boolean hasKeyboard(List<ExternalDevice> devices) {
        for (ExternalDevice device : devices) {
            if (device.getDeviceType() == Constants.TYPE_DEVICE_KEYBOARD) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断外接设备
     */
    public static ExternalDevice buildExternalDevice(InputDevice device, int deviceType) {
        ExternalDevice externalDevice = new ExternalDevice();
        externalDevice.setProductName(device.getName());
        externalDevice.setManufacturerName(device.getName());
        externalDevice.setDeviceId(device.getProductId());
        externalDevice.setDeviceType(deviceType);
        return externalDevice;
    }
}
