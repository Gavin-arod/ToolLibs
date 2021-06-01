package com.tool.lib.impl;

public interface CurrentNetSpeedListener {
    /**
     * @param loadProgress 下载文件百分比
     * @param netSpeed     实时网速
     */
    void getRealTimeNetSpeed(int loadProgress, String netSpeed);

    /**
     * 测速完成
     */
    void netDetectionDone(String averageNetSpeed);
}
