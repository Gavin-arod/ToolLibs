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

package com.tool.view.board.renderer;

/**
 * @ClassName XEnum
 * @Description 枚举定义
 */

public class XEnum {
    /**
     * 横向显示位置,靠左，中间，还是靠右(如图标题......)
     */
    public enum HorizontalAlign {
        LEFT, CENTER, RIGHT
    }

    /**
     * 竖向显示位置,上方，中间，底部 (如坐标轴标签......)
     */
    public enum VerticalAlign {
        TOP, MIDDLE, BOTTOM
    }

    /**
     * 三角形的朝向
     */
    public enum TriangleDirection {
        UP, DOWN, LEFT, RIGHT
    }

    /**
     * 三角形填充风格
     */
    public enum TriangleStyle {
        OUTLINE, FILL
    }


    /**
     * 线的几种显示风格:Solid、Dot、Dash
     */
    public enum LineStyle {
        SOLID, DOT, DASH
    }

    /**
     * 框的类型
     */
    public enum RectType {
        RECT, ROUNDRECT
    }

    /**
     * 横向或竖向网格线
     */
    public enum Direction {
        HORIZONTAL, VERTICAL
    }

    /**
     * 设置圆形轴的类型
     */
    //DialChart RoundAxis
    public enum RoundAxisType {
        TICKAXIS, RINGAXIS, ARCLINEAXIS, FILLAXIS, CIRCLEAXIS, LINEAXIS
    }


    /**
     * 位置
     */
    public enum Location {
        TOP, BOTTOM, LEFT, RIGHT
    }

    /**
     * 指针类型
     */
    public enum PointerStyle {
        TRIANGLE, LINE
    }

    /**
     * 设置圆形Tick轴的类型
     */
    public enum RoundTickAxisType {
        INNER_TICKAXIS, OUTER_TICKAXIS
    }

    /**
     * 手势平移模式(模向，纵向，自由移动)
     */
    public enum PanMode {
        HORIZONTAL, VERTICAL, FREE
    }
}
