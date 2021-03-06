package com.auguigu.lijingxin.mobileplayer.Utils;

import java.util.Formatter;
import java.util.Locale;

/**
 * 作者：尚硅谷-杨光福 on 2016/9/6 15:27
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：工具类
 */
public class Utils {
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;

    public Utils() {
        // 转换成字符串的时间
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

    }

    /**
     * 把毫秒转换成：1:20:30这里形式
     * @param timeMs
     * @return
     */
    public String stringForTime(Long timeMs) {
        Long  totalSeconds = timeMs / 1000;
        Long  seconds = totalSeconds % 60;

        Long  minutes = (totalSeconds / 60) % 60;

        Long  hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds)
                    .toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
}
