package com.tool;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.tool.base.BaseActivity;
import com.tool.lib.impl.CurrentNetSpeedListener;
import com.tool.lib.manager.DetectionManager;
import com.tool.util.FastClickUtil;
import com.tool.util.MathUtil;
import com.tool.view.DialChartBoardView;

/**
 * 实时网速检测页
 */
public class RealtimeNetSpeedActivity extends BaseActivity implements View.OnClickListener {
    private DialChartBoardView boardView;
    private AppCompatTextView tvNetSpeedTipText;
    //正在测速状态:默认为false
    private boolean isSpeedTesting = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_detection);

        boardView = findViewById(R.id.net_speed_board);
        AppCompatButton btnStartNetTest = findViewById(R.id.btn_retest_speed);
        AppCompatButton btnCancelNetTest = findViewById(R.id.btn_cancel_net_detect);
        tvNetSpeedTipText = findViewById(R.id.tv_net_speed_tip);
        btnStartNetTest.requestFocus();

        btnStartNetTest.setOnClickListener(this);
        btnCancelNetTest.setOnClickListener(this);
        findViewById(R.id.btn_white_back).setOnClickListener(this);

        //开始测速
        startDetection();
    }


    @Override
    public void onClick(View v) {
        if (FastClickUtil.isFastDoubleClick()) {
            return;
        }

        if (v.getId() == R.id.btn_retest_speed) {
            if (isSpeedTesting) {
                Toast.makeText(RealtimeNetSpeedActivity.this, "正在测速中", Toast.LENGTH_SHORT).show();
                return;
            }
            //测速进程正在执行，避免开启多个下载进程
            if (DetectionManager.defaultManager().netDetectionIsRunning()) {
                DetectionManager.defaultManager().cancelDetectNetSpeed();
            }

            startDetection();
        } else if (v.getId() == R.id.btn_cancel_net_detect ||
                v.getId() == R.id.btn_white_back) {
            //取消网络测速
            DetectionManager.defaultManager().cancelDetectNetSpeed();
            finish();
        }
    }

    //开始网速检测
    private void startDetection() {
        DetectionManager.defaultManager()
                .startDetectNetSpeed(new CurrentNetSpeedListener() {
                    @Override
                    public void getRealTimeNetSpeed(int loadProgress, String netSpeed) {
                        isSpeedTesting = true;
                        runOnUiThread(() -> {
                            float curNetSpeed = Float.parseFloat(netSpeed);
                            float curAngle = calculateAngle(curNetSpeed);
                            boardView.setCurrentStatus(curNetSpeed, curAngle);
                            boardView.invalidate();
                            tvNetSpeedTipText.setText("测速中...");
                        });
                    }

                    @Override
                    public void netDetectionDone(String averageNetSpeed) {
                        isSpeedTesting = false;
                        runOnUiThread(() -> {
                            float averageSpeed = Float.parseFloat(averageNetSpeed);
                            float curAngle = calculateAngle(averageSpeed);
                            boardView.setCurrentStatus(averageSpeed, curAngle);
                            boardView.invalidate();

                            //网速小于1M
                            String endText;
                            if (averageSpeed < 1) {
                                averageSpeed = averageSpeed * 1024;
                                endText = "K/s";
                            } else {
                                endText = "M/s";
                            }
                            String text = "测速完成，当前网速" + MathUtil.round(averageSpeed, 2) + endText;
                            tvNetSpeedTipText.setText(text);
                        });
                    }
                });
    }

    /**
     * 计算当前网速下指针应该指向的对应角度
     */
    private float calculateAngle(float netSpeed) {
        //计算当前网速下指针应该指向的对应角度
        float curAngle;
        float totalAngle = 270f;
        //270°按照刻度值集合长度等分，baseAngle为每一份的角度值
        float baseAngle = (1 / (float) (boardView.getLabels().size() - 1)) * totalAngle;
        if (netSpeed >= 0 && netSpeed < 1) {
            curAngle = 0 + netSpeed * (1 / 8f) * totalAngle;
        } else if (netSpeed >= 1 && netSpeed < 2) {
            curAngle = (1 / 8f) * totalAngle + (netSpeed - 1) * baseAngle;
        } else if (netSpeed >= 2 && netSpeed < 4) {
            curAngle = (2 / 8f) * totalAngle + (netSpeed / (4 - 2) - 1) * baseAngle;
        } else if (netSpeed >= 4 && netSpeed < 8) {
            curAngle = (3 / 8f) * totalAngle + (netSpeed / (8 - 4) - 1) * baseAngle;
        } else if (netSpeed >= 8 && netSpeed < 10) {
            curAngle = (4 / 8f) * totalAngle + ((netSpeed - 8 + (10 - 8)) / (10 - 8) - 1) * baseAngle;
        } else if (netSpeed >= 10 && netSpeed < 20) {
            curAngle = (5 / 8f) * totalAngle + (netSpeed / (20 - 10) - 1) * baseAngle;
        } else if (netSpeed >= 20 && netSpeed < 50) {
            curAngle = (6 / 8f) * totalAngle + ((netSpeed - 20 + (50 - 20)) / (50 - 20) - 1) * baseAngle;
        } else if (netSpeed >= 50 && netSpeed < 100) {
            curAngle = (7 / 8f) * totalAngle + (netSpeed / (100 - 50) - 1) * baseAngle;
        } else {
            curAngle = 270;
        }
        return curAngle;
    }
}
