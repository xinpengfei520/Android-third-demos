package com.atguigu.myselect;

import android.content.Context;

/**
 * dip与px相互转换的工具类
 * @author shkstart
 */
public class DensityUtils {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float density =
                context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float density =
                context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / density + 0.5f);
    }
}

