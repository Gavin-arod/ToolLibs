package com.tool.util;

public class FastClickUtil {
    //按钮快速点击判断
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        lastClickTime = time;
        return timeD <= 500;
    }
}
