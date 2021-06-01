package com.tool.lib.manager;

import android.util.Log;

import com.tool.lib.constants.Constants;
import com.tool.lib.impl.CurrentNetSpeedListener;
import com.tool.lib.utils.Util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 网速检测进度
 */
public class NetSpeedDetectionProgress {
    //开始下载的时间
    private static long startDownTime;
    //开始下载时间：用于计算平均网速
    private static long beginDownloadTime;
    //当前下载文件的时间
    private static long currentLoadingTime;
    //上一次下载的文件长度
    private static long preBytesRead;
    //1s后下载的文件长度
    private static int currentLength;

    private Call mCall;

    private CurrentNetSpeedListener currentNetSpeedListener;

    public void setCurrentNetSpeedListener(CurrentNetSpeedListener currentNetSpeedListener) {
        this.currentNetSpeedListener = currentNetSpeedListener;
    }

    private void reset() {
        startDownTime = 0L;
        beginDownloadTime = 0L;
        currentLoadingTime = 0L;
        preBytesRead = 0L;
        currentLength = 0;
    }

    public void run() {
        reset();

        Request request = new Request.Builder()
                .url(Constants.URL_DOWNLOAD)
                .build();

        final ProgressListener progressListener = new ProgressListener() {
            boolean firstUpdate = true;

            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                if (done) {
                    //检测完成,计算平均网速
                    String averageNetSpeed = calculateAverageNetSpeed(contentLength);
                    currentNetSpeedListener.netDetectionDone(averageNetSpeed);
                    cancel();
                    Log.e("平均网速：", averageNetSpeed);
                    System.out.println("completed");
                } else {
                    if (firstUpdate) {
                        firstUpdate = false;
                        if (contentLength == -1) {
                            System.out.println("content-length: unknown");
                        } else {
                            System.out.format("content-length: %d\n", contentLength);
                        }
                    }

                    if (contentLength != -1) {
                        currentLoadingTime = System.currentTimeMillis();
                        if (startDownTime == 0) {
                            startDownTime = currentLoadingTime;
                            beginDownloadTime = currentLoadingTime;
                        }

                        //计算是否到达限制时间:时间限制为15s
                        if (currentLoadingTime - beginDownloadTime > 15 * 1000) {
                            String averageNetSpeed = calculateAverageNetSpeed(bytesRead);
                            currentNetSpeedListener.netDetectionDone(averageNetSpeed);
                            //退出下载进程
                            cancel();
                            return;
                        }

                        //计算进度
                        int loadProgress = calculateLoadProgress(currentLoadingTime, beginDownloadTime, 15 * 1000);
                        //计算实时网速：1s更新一次网速
                        if (currentLoadingTime - startDownTime >= 1000) {
                            String usageTime = Util.txFloat((int) (currentLoadingTime - startDownTime), 1000, "0.0000");
                            float intUsageTime = Float.parseFloat(usageTime);

                            if (preBytesRead == 0) {
                                currentLength = (int) ((bytesRead) / intUsageTime);
                                preBytesRead = bytesRead;
                            } else {
                                currentLength = (int) ((bytesRead - preBytesRead) / intUsageTime);
                            }

                            preBytesRead = bytesRead;
                            startDownTime = currentLoadingTime;

                            //计算实时网速
                            String netSpeed = setCurNetSpeed(currentLength);
                            currentNetSpeedListener.getRealTimeNetSpeed(loadProgress, netSpeed);
                        }
                    }
                }
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(chain -> {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                            .build();
                })
                .build();


        mCall = client.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e.getMessage() != null) {
                    Log.e("Unexpected code：", e.getMessage());
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    try {
                        ResponseBody responseBody = response.body();
                        if (responseBody != null) {
                            ByteString byteString = responseBody.byteString();
                            Log.e("isSuccessful：", byteString.size() + "");
                        }
//                        String str = response.body().string();
//                        Log.e("isSuccessful：", str + "");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private static class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private final ProgressListener progressListener;
        private BufferedSource bufferedSource;

        ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @NotNull
        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(@NotNull Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }

    interface ProgressListener {
        void update(long bytesRead, long contentLength, boolean done);
    }

    //计算平均网速
    public String calculateAverageNetSpeed(long currentLength) {
        //下载完成的时间
        long endLoadingTime = System.currentTimeMillis();
        //平均每秒下载的长度
        int loadedLengthPerSec = (int) (currentLength / ((endLoadingTime - beginDownloadTime) / 1000));
        return Util.txFloat(loadedLengthPerSec, 1024 * 1024, "0.000");
    }

    //计算实时网速
    public String setCurNetSpeed(int loadedLength) {
        return Util.txFloat(loadedLength, 1024 * 1024, "0.000");
    }

    //计算检测进度
    public int calculateLoadProgress(long currentTime, long beginTime, long limitTime) {
        String progress = Util.txFloat((int) (currentTime - beginTime), limitTime, "0.000");
        return (int) (Float.parseFloat(progress) * 100);
    }

    //取消下载任务
    public void cancel() {
        if (mCall != null && mCall.isExecuted()) {
            mCall.cancel();
            mCall = null;
        }
    }

    //Call正在执行
    public boolean isRunning() {
        return mCall != null && mCall.isExecuted();
    }

}
