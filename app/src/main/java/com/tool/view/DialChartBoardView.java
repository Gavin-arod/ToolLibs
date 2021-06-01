/*
 * Copyright 2014  XCL-Charts
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @Project XCL-Charts
 * @Description Android图表基类库
 * @author XiongChuanLiang<br />(xcl_168@aliyun.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version v0.1
 */

package com.tool.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.tool.R;
import com.tool.view.board.chart.DialChart;
import com.tool.view.board.common.MathHelper;
import com.tool.view.board.renderer.XEnum;
import com.tool.view.board.renderer.plot.PlotAttrInfo;
import com.tool.view.board.renderer.plot.Pointer;
import com.tool.view.board.view.GraphicalView;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络测速仪表盘
 */
public class DialChartBoardView extends GraphicalView {
    private final String TAG = "DialChartBoardView";

    private final DialChart chart = new DialChart();
    //当前网速
    private float mCurNetSpeed = 0f;
    //指针指向的角度
    private float mCurAngle = 0f;
    //刻度值集合
    private List<String> rLabels;

    public DialChartBoardView(Context context) {
        super(context);
        initView();
    }

    public DialChartBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DialChartBoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        chartRender();
    }

    //获取刻度值集合长度
    public List<String> getLabels() {
        return rLabels;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        chart.setChartRange(w, h);
    }

    public void chartRender() {
        try {
            //设置标题背景
            chart.setApplyBackgroundColor(false);
            chart.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
            //绘制边框
            chart.hideBorder();
            //设置当前百分比
            chart.getPointer().setPercentage(mCurAngle / 270f);
            //设置指针长度
            chart.getPointer().setLength(0.7f);

            //增加轴
            addAxis();
            /////////////////////////////////////////////////////////////
            //增加指针
            addPointer();
            //设置附加信息
            addAttrInfo();
            /////////////////////////////////////////////////////////////
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 增加环形空心轴
     * 角度为270
     */
    public void addAxis() {
        List<Float> ringPercentage = new ArrayList<>();
        //相当于40%	//270, 4
        float rPer = MathHelper.getInstance().div(1, 4);
        ringPercentage.add(rPer);
        ringPercentage.add(rPer);
        ringPercentage.add(rPer);
        ringPercentage.add(rPer);

        List<Integer> rColor = new ArrayList<>();
        rColor.add(ContextCompat.getColor(getContext(), R.color.color_white_80));
        rColor.add(ContextCompat.getColor(getContext(), R.color.color_white_80));
        rColor.add(ContextCompat.getColor(getContext(), R.color.color_white_80));
        rColor.add(ContextCompat.getColor(getContext(), R.color.color_white_80));
        chart.addStrokeRingAxis(0.95f, 0.8f, ringPercentage, rColor);

        rLabels = new ArrayList<>();
        rLabels.add("0");
        rLabels.add("1");
        rLabels.add("2");
        rLabels.add("4");
        rLabels.add("8");
        rLabels.add("10");
        rLabels.add("20");
        rLabels.add("50");
        rLabels.add("100");
        chart.addInnerTicksAxis(0.8f, rLabels);

        chart.getPlotAxis().get(0).getFillAxisPaint().setColor(ContextCompat.getColor(getContext(), R.color.color_black_80));
        chart.getPlotAxis().get(1).getFillAxisPaint().setColor(ContextCompat.getColor(getContext(), R.color.color_black_80));
        chart.getPlotAxis().get(1).getTickLabelPaint().setColor(ContextCompat.getColor(getContext(), R.color.white));
        chart.getPlotAxis().get(1).getTickMarksPaint().setColor(ContextCompat.getColor(getContext(), R.color.white));
        chart.getPlotAxis().get(1).hideAxisLine();

        chart.getPointer().setPointerStyle(XEnum.PointerStyle.TRIANGLE);
        chart.getPointer().getPointerPaint().setColor(ContextCompat.getColor(getContext(), R.color.color_fad8a1));
        chart.getPointer().getPointerPaint().setStrokeWidth(3);
        chart.getPointer().getPointerPaint().setStyle(Style.FILL);
        chart.getPointer().showBaseCircle();
    }

    //增加指针
    public void addPointer() {
        chart.addPointer();
        List<Pointer> mp = chart.getPlotPointer();
        mp.get(0).setPercentage(mCurAngle / 270f);
        //设置指针长度
        mp.get(0).setLength(0.7f);
        mp.get(0).getPointerPaint().setColor(ContextCompat.getColor(getContext(), R.color.color_fad8a1));
        mp.get(0).setPointerStyle(XEnum.PointerStyle.TRIANGLE);
        mp.get(0).showBaseCircle();
    }

    private void addAttrInfo() {
        PlotAttrInfo plotAttrInfo = chart.getPlotAttrInfo();
        //设置附加信息
        Paint paintTB = new Paint();
        paintTB.setColor(Color.WHITE);
        paintTB.setTextAlign(Align.CENTER);
        paintTB.setTextSize(24);
        paintTB.setAntiAlias(true);
        plotAttrInfo.addAttributeInfo(XEnum.Location.TOP, "当前网速", 0.3f, paintTB);

        Paint paintBT = new Paint();
        paintBT.setColor(Color.WHITE);
        paintBT.setTextAlign(Align.CENTER);
        paintBT.setTextSize(32);
        paintBT.setFakeBoldText(true);
        paintBT.setAntiAlias(true);
        //网速小于1M
        if (mCurNetSpeed < 1) {
            plotAttrInfo.addAttributeInfo(XEnum.Location.BOTTOM,
                    Float.toString(MathHelper.getInstance().round(mCurNetSpeed * 1024, 2)), 0.38f, paintBT);
        } else {
            plotAttrInfo.addAttributeInfo(XEnum.Location.BOTTOM,
                    Float.toString(MathHelper.getInstance().round(mCurNetSpeed, 2)), 0.38f, paintBT);
        }

        Paint paintBT2 = new Paint();
        paintBT2.setColor(Color.WHITE);
        paintBT2.setTextAlign(Align.CENTER);
        paintBT2.setTextSize(24);
        paintBT2.setFakeBoldText(true);
        paintBT2.setAntiAlias(true);
        //网速小于1M，用K/s表示
        if (mCurNetSpeed < 1f) {
            plotAttrInfo.addAttributeInfo(XEnum.Location.BOTTOM, "K/s", 0.50f, paintBT2);
        } else {
            plotAttrInfo.addAttributeInfo(XEnum.Location.BOTTOM, "M/s", 0.50f, paintBT2);
        }
    }

    public void setCurrentStatus(float netSpeed, float angle) {
        mCurNetSpeed = netSpeed;
        mCurAngle = angle;
        chart.clearAll();

        //设置当前百分比
        chart.getPointer().setPercentage(angle / 270f);
        addAxis();
        //增加指针
        addPointer();
        addAttrInfo();
    }


    @Override
    public void render(Canvas canvas) {
        // TODO Auto-generated method stub
        try {
            chart.render(canvas);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

}