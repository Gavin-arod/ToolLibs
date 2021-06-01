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
 * <p>
 * version	 	date
 * 0.1 		2014-06-12
 * 1.0 		2014-07-04
 * 1.2			2014-07-13
 * <p>
 * version	 	date
 * 0.1 		2014-06-12
 * 1.0 		2014-07-04
 * 1.2			2014-07-13
 */

package com.tool.view.board;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.tool.view.board.common.MathHelper;
import com.tool.view.board.renderer.XEnum;
import com.tool.view.board.renderer.plot.BorderRender;
import com.tool.view.board.renderer.plot.PlotArea;
import com.tool.view.board.renderer.plot.PlotAreaRender;
import com.tool.view.board.renderer.plot.PlotTitleRender;

/**
 * @ClassName XChart
 * @Description 所有图表类的基类, 定义了图表区，标题，背景等
 */
public class XChart implements IRender {
    // 开放主图表区
    protected PlotAreaRender plotArea = null;
    // 标题栏
    private PlotTitleRender plotTitle = null;
    // 图大小范围
    private float mLeft = 0.0f;
    private float mTop = 0.0f;
    private float mRight = 0.0f;
    private float mBottom = 0.0f; //5f;
    // 图宽高
    private float mWidth = 0.0f;
    private float mHeight = 0.0f;

    // 图的内边距属性
    private float mPaddingTop = 0.f;
    private float mPaddingBottom = 0.f;
    private float mPaddingLeft = 0.f;
    private float mPaddingRight = 0.f;
    // 是否画背景色
    private boolean mBackgroundColorVisible = false;

    //坐标系原点坐标
    protected float[] mTranslateXY = new float[2];

    //是否显示边框
    private boolean mShowBorder = false;
    private BorderRender mBorder = null;

    private final boolean mEnableScale = true;
    private float mXScale = 0.0f, mYScale = 0.0f;
    private float mCenterX = 0.0f, mCenterY = 0.0f;

    //是否平移
    protected boolean mEnablePanMode = true;
    //平移模式下的可移动方向
    private final XEnum.PanMode mPlotPanMode = XEnum.PanMode.FREE;


    public XChart() {
        initChart();
    }

    private void initChart() {
        //默认的原点坐标
        mTranslateXY[0] = 0.0f;
        mTranslateXY[1] = 0.0f;

        // 图表
        if (null == plotArea) plotArea = new PlotAreaRender();

        if (null == plotTitle) plotTitle = new PlotTitleRender();
    }

    /**
     * 返回主图表区基类
     *
     * @return 主图表区基类
     */
    public PlotArea getPlotArea() {
        if (null == plotArea) plotArea = new PlotAreaRender();
        return plotArea;
    }

    /**
     * 设置图表绘制范围.
     *
     * @param width  图表宽度
     * @param height 图表高度
     */
    public void setChartRange(float width, float height) {
        setChartRange(0.0f, 0.0f, width, height);
    }


    /**
     * 设置图表绘制范围,以指定起始点及长度方式确定图表大小.
     *
     * @param startX 图表起点X坐标
     * @param startY 图表起点Y坐标
     * @param width  图表宽度
     * @param height 图表高度
     */
    public void setChartRange(float startX, float startY, float width,
                              float height) {

        if (startX > 0)
            mLeft = startX;
        if (startY > 0)
            mTop = startY;

        mRight = add(startX, width);
        mBottom = add(startY, height);

        if (Float.compare(width, 0.0f) > 0) mWidth = width;
        if (Float.compare(height, 0.0f) > 0) mHeight = height;
    }

    /**
     * 返回图表左边X坐标
     *
     * @return 左边X坐标
     */
    public float getLeft() {
        return mLeft;
    }

    /**
     * 返回图表上方Y坐标
     *
     * @return 上方Y坐标
     */
    public float getTop() {
        return mTop;
    }

    /**
     * 返回图表右边X坐标
     *
     * @return 右边X坐标
     */
    public float getRight() {
        return mRight;
    }

    /**
     * 返回图表底部Y坐标
     *
     * @return 底部Y坐标
     */
    public float getBottom() {
        return mBottom;
    }

    /**
     * 计算图的显示范围,依屏幕px值来计算.
     */
    protected void calcPlotRange() {
        int borderWidth = getBorderWidth();
        if (null == plotArea) return;
        plotArea.setBottom(sub(this.getBottom() - borderWidth / 2f, mPaddingBottom));
        plotArea.setLeft(add(this.getLeft() + borderWidth / 2f, mPaddingLeft));
        plotArea.setRight(sub(this.getRight() - borderWidth / 2f, mPaddingRight));
        plotArea.setTop(add(this.getTop() + borderWidth / 2f, mPaddingTop));
    }

    /**
     * 绘制标题
     */
    protected void renderTitle(Canvas canvas) {
        int borderWidth = getBorderWidth();
        if (null == plotTitle) return;
        this.plotTitle.renderTitle(
                mLeft + borderWidth, mRight - borderWidth, mTop + borderWidth,
                mWidth, this.plotArea.getTop(), canvas);
    }

    /**
     * 设置是否绘制背景
     *
     * @param visible 是否绘制背景
     */
    public void setApplyBackgroundColor(boolean visible) {
        mBackgroundColorVisible = visible;
    }

    /**
     * 设置图的背景色
     *
     * @param color 背景色
     */
    public void setBackgroundColor(int color) {

        getBackgroundPaint().setColor(color);
        getPlotArea().getBackgroundPaint().setColor(color);

        if (null == mBorder) mBorder = new BorderRender();
        mBorder.getBackgroundPaint().setColor(color);
    }

    /**
     * 开放背景画笔
     *
     * @return 画笔
     */
    public Paint getBackgroundPaint() {
        if (null == mBorder) mBorder = new BorderRender();
        return mBorder.getBackgroundPaint();
    }

    /**
     * 隐藏边框
     */
    public void hideBorder() {
        mShowBorder = false;
        if (null != mBorder) mBorder = null;
    }

    /**
     * 得到边框宽度,默认为5px
     *
     * @return 边框宽度
     */
    public int getBorderWidth() {
        int borderWidth = 0;
        if (mShowBorder) {
            if (null == mBorder) mBorder = new BorderRender();
            borderWidth = mBorder.getBorderWidth();
        }
        return borderWidth;
    }

    /**
     * 绘制边框
     *
     * @param canvas 画布
     */
    protected void renderBorder(Canvas canvas) {
        if (mShowBorder) {
            if (null == mBorder) mBorder = new BorderRender();
            mBorder.renderBorder("BORDER", canvas,
                    mLeft, mTop, mRight, mBottom);
        }
    }

    /**
     * 绘制图的背景
     */
    protected void renderChartBackground(Canvas canvas) {
        if (this.mBackgroundColorVisible) {
            if (null == mBorder) mBorder = new BorderRender();
            if (mShowBorder) {
                mBorder.renderBorder("CHART", canvas,
                        mLeft, mTop, mRight, mBottom);
            } else { //要清掉 border的默认间距
                int borderSpadding = mBorder.getBorderSpadding();
                mBorder.renderBorder("CHART", canvas,
                        mLeft - borderSpadding, mTop - borderSpadding,
                        mRight + borderSpadding, mBottom + borderSpadding);
            }
        }
    }

    /**
     * 缩放图表
     *
     * @param canvas 画布
     */
    private void scaleChart(Canvas canvas) {
        if (!mEnableScale) return;

        if (Float.compare(mCenterX, 0.0f) == 1 ||
                Float.compare(mCenterY, 0.0f) == 1) {
            canvas.scale(mXScale, mYScale, mCenterX, mCenterY);
            //}else{
            //canvas.scale(mScale, mScale,plotArea.getCenterX(),plotArea.getCenterY());
        }
    }

    /**
     * 返回当前图表平移模式
     *
     * @return 平移模式
     */
    public XEnum.PanMode getPlotPanMode() {
        return mPlotPanMode;
    }

    /**
     * 返回当前图表的平移状态
     *
     * @return 平移状态
     */
    public boolean getPanModeStatus() {
        return mEnablePanMode;
    }

    /**
     * 用于延迟绘制
     *
     * @param canvas 画布
     * @return 是否成功
     * @throws Exception 例外
     */
    protected boolean postRender(Canvas canvas) throws Exception {
        try {
            // 绘制图背景
            renderChartBackground(canvas);

        } catch (Exception e) {
            throw e;
        }
        return true;
    }


    @Override
    public void render(Canvas canvas) throws Exception {
        // TODO Auto-generated method stubcalcPlotRange
        boolean ret;
        try {

            if (null == canvas)
                return;


            canvas.save();

            //缩放图表
            scaleChart(canvas);

            //绘制图表
            ret = postRender(canvas);

            //绘制边框
            renderBorder(canvas);

            canvas.restore();


        } catch (Exception e) {
            throw e;
        }
    }


    //math计算类函数 ----------------------------------------------------------------
    /**
     * 加法运算
     *
     * @param v1 参数1
     * @param v2 参数2
     * @return 结果
     */
    protected float add(float v1, float v2) {
        return MathHelper.getInstance().add(v1, v2);
    }

    /**
     * 减法运算
     *
     * @param v1 参数1
     * @param v2 参数2
     * @return 运算结果
     */
    protected float sub(float v1, float v2) {
        return MathHelper.getInstance().sub(v1, v2);
    }

    /**
     * 乘法运算
     *
     * @param v1 参数1
     * @param v2 参数2
     * @return 运算结果
     */
    protected float mul(float v1, float v2) {
        return MathHelper.getInstance().mul(v1, v2);
    }

    /**
     * 除法运算,当除不尽时，精确到小数点后10位
     *
     * @param v1 参数1
     * @param v2 参数2
     * @return 运算结果
     */
    protected float div(float v1, float v2) {
        return MathHelper.getInstance().div(v1, v2);
    }

    //math计算类函数 ----------------------------------------------------------------

}
