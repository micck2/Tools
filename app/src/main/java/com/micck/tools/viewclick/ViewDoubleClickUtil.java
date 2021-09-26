package com.micck.tools.viewclick;

import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * hook处理view快速点击问题
 *
 * @author lilin
 * @time on 2021/9/24 5:53 下午
 */
public class ViewDoubleClickUtil {

    public static void hookView(View view) {
        try {
            // 得到 View 的 ListenerInfo 对象
            Method getListenerInfo = View.class.getDeclaredMethod("getListenerInfo");
            //修改getListenerInfo为可访问(View中的getListenerInfo不是public)
            getListenerInfo.setAccessible(true);
            Object listenerInfo = getListenerInfo.invoke(view);
            // 得到 原始的 OnClickListener 对象
            Class<?>  listenerInfoClz = Class.forName("android.view.View$ListenerInfo");
            Field mOnClickListener = listenerInfoClz.getDeclaredField("mOnClickListener");
            mOnClickListener.setAccessible(true);
            View.OnClickListener originOnClickListener = (View.OnClickListener) mOnClickListener.get(listenerInfo);
            // 用自定义的 OnClickListener 替换原始的 OnClickListener
            View.OnClickListener hookedOnClickListener = new OnClickListenerProxy(originOnClickListener);
            mOnClickListener.set(listenerInfo, hookedOnClickListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //自定义的代理事件监听器
    private static class OnClickListenerProxy implements View.OnClickListener {

        private final View.OnClickListener object;

        private final static int MIN_CLICK_DELAY_TIME = 1000;

        private long lastClickTime = 0;

        private OnClickListenerProxy(View.OnClickListener object) {
            this.object = object;
        }

        @Override
        public void onClick(View v) {
            //点击时间控制
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                Log.e("OnClickListenerProxy", "点击view: "+ v);
                if (object != null) object.onClick(v);
            } else {
                Log.e("OnClickListenerProxy", "重复点击，已过滤");
            }
        }
    }
}
