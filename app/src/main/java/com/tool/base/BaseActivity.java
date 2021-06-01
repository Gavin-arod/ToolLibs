package com.tool.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tool.util.ImmersiveBarUtil;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式
        ImmersiveBarUtil.hideBar(getWindow().getDecorView());
        ImmersiveBarUtil.setNavigationStatusColor(getWindow());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            //沉浸式
            ImmersiveBarUtil.hideBar(getWindow().getDecorView());
            ImmersiveBarUtil.setNavigationStatusColor(getWindow());
        }
    }
}
