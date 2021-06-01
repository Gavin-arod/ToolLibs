/*
  Copyright 2014  XCL-Charts
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.

  @Project XCL-Charts
 * @Description Android图表基类库
 * @author XiongChuanLiang<br />(xcl_168@aliyun.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */

package com.tool.view.board.chart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;

import com.tool.view.board.XChart;

/**
 * @ClassName CirChart
 * @Description 圆形类图表，如饼图，刻度盘...类的图表的基类
 */

public class CirChart extends XChart {
    //半径
    private float mRadius = 0.0f;

    //标签注释显示位置 [隐藏,Default,Inside,Ouside,Line]

    //开放标签画笔让用户设置
    private Paint mPaintLabel = null;

    public CirChart() {
        //初始化图例

    }

    @Override
    protected void calcPlotRange() {
        super.calcPlotRange();

        this.mRadius = Math.min(div(this.plotArea.getWidth(), 2f),
                div(this.plotArea.getHeight(), 2f));
    }


    /**
     * 返回半径
     *
     * @return 半径
     */
    public float getRadius() {
        return mRadius;
    }

    /**
     * 开放标签画笔
     *
     * @return 画笔
     */
    public Paint getLabelPaint() {
        if (null == mPaintLabel) {
            mPaintLabel = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaintLabel.setColor(Color.BLACK);
            mPaintLabel.setAntiAlias(true);
            mPaintLabel.setTextAlign(Align.CENTER);
            mPaintLabel.setTextSize(18);
        }
        return mPaintLabel;
    }

    @Override
    protected boolean postRender(Canvas canvas) throws Exception {
        try {
            super.postRender(canvas);

            //计算主图表区范围
            calcPlotRange();
            //画Plot Area背景
            plotArea.render(canvas);
            //绘制标题
            renderTitle(canvas);
        } catch (Exception e) {
            throw e;
        }
        return true;
    }

    @Override
    public void render(Canvas canvas) throws Exception {
        // TODO Auto-generated method stubcalcPlotRange
        try {
            if (null == canvas)
                return;

            if (getPanModeStatus()) {
                canvas.save();
                //设置原点位置
                switch (this.getPlotPanMode()) {
                    case HORIZONTAL:
                        canvas.translate(mTranslateXY[0], 0);
                        break;
                    case VERTICAL:
                        canvas.translate(0, mTranslateXY[1]);
                        break;
                    default:
                        canvas.translate(mTranslateXY[0], mTranslateXY[1]);
                        break;
                }

                //绘制图表
                super.render(canvas);

                //还原
                canvas.restore();
            } else {
                //绘制图表
                super.render(canvas);
            }

        } catch (Exception e) {
            throw e;
        }
    }


}
