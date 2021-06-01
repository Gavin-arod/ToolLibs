package com.tool.util;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 沉浸式布局
 */
public class ImmersiveBarUtil {
    /**
     * 隐藏状态栏导航栏
     */
    public static void hideBar(View decorView) {
        if (decorView != null) {
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.INVISIBLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 状态栏导航栏背景设置透明
     */
    public static void setNavigationStatusColor(Window window) {
        //VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setNavigationBarColor(Color.TRANSPARENT);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }
    }
}
