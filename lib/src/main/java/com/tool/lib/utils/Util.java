package com.tool.lib.utils;

import java.text.DecimalFormat;

public class Util {
    //除以保留小数
    public static String txFloat(int a, int b, String digits) {
        DecimalFormat df = new DecimalFormat(digits);//设置保留位数
        return df.format((float) a / b);
    }

    public static String txFloat(long a, long b, String digits) {
        DecimalFormat df = new DecimalFormat(digits);//设置保留位数
        return df.format((float) a / b);
    }
}
