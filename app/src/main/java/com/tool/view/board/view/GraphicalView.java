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
 * @version 1.7
 */
package com.tool.view.board.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @ClassName GraphicalView
 * @Description XCL-Charts图表的View基类
 */
public abstract class GraphicalView extends View {

    public GraphicalView(Context context) {
        super(context);

    }

    public GraphicalView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public GraphicalView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 绘制图表
     *
     * @param canvas 画布
     */
    public abstract void render(Canvas canvas);

    @Override
    public void onDraw(Canvas canvas) {
        try {
            render(canvas);
        } catch (Exception e) {
            Log.e("GraphicalView", e.toString());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int result = 100;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) { //fill_parent
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) { //wrap_content
            result = Math.min(result, specSize);
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 100;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) { //fill_parent
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) { //wrap_content
            result = Math.min(result, specSize);
        }
        return result;
    }
}
