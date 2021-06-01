package com.tool;

import android.content.Intent;
import android.os.Bundle;

import com.tool.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_external_device).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ExternalActivity.class)));

        findViewById(R.id.btn_realtime_internet_speed).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, RealtimeNetSpeedActivity.class)));

    }
}