package com.auguigu.lijingxin.p2p.Utils;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.auguigu.lijingxin.p2p.MyApplication.MyApplication;

/**
 * 作者：李婧馨 on 2016/9/19 16:27
 * 电话: 15180176050
 * QQ号：1109711383
 * 作用：专门提供为处理一些UI相关的问题而创建的工具类,
 * 提供资源获取的通用方法,避免每次都写重复的代码获取结果.
 */
public class UIUtils {

    /**
     * 返回当前应用context实例
     *
     * @return
     */
    public static Context getContext() {
        return MyApplication.mContext;
    }

    /**
     * 返回可以发送消息的handler的实例
     *
     * @return
     */
    public static Handler getHandler() {
        return MyApplication.mHandler;
    }

    /**
     * 返回资源文件中指定的colorID对应的颜色值
     *
     * @param colorId
     * @return
     */
    public static int getColor(int colorId) {
        return getContext().getResources().getColor(colorId);
    }

    /**
     * 加载指定layoutId的布局,并返回
     *
     * @param layoutId
     * @return
     */
    public static View getXmlView(int layoutId) {
        return View.inflate(getContext(), layoutId, null);
    }

    /**
     * 返回对应的string.xml中<string-array>对应的string数组资源
     *
     * @param stringArrayId
     * @return
     */
    public static String[] getStringArray(int stringArrayId) {
        String[] stringArray = getContext().getResources().getStringArray(stringArrayId);
        return stringArray;
    }

    /**
     * 将dp转换为px
     *
     * @param dp
     * @return
     */
    public static int dp2px(int dp) {//px=dp*density  (160dp手机看成density = 1)
        //获取当前手机的密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);//通过四舍五入，获取最接近的整数
    }

    /**
     * 将px转换为dp
     *
     * @param px
     * @return
     */
    public static int px2dp(int px) {//px=dp*density  (160dp手机看成density = 1)
        //获取当前手机的密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5);//通过四舍五入，获取最接近的整数
    }
}
