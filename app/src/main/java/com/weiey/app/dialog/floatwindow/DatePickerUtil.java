package com.weiey.app.dialog.floatwindow;

import android.content.Context;
import com.weiey.app.utils.Callback;
import com.weiey.app.view.datepicker.DatePicker;

import java.util.Calendar;

public class DatePickerUtil {
    /**
     * 选择 格式为 yyyy-MM-dd
     * @param mContext
     * @param time
     * @param callback
     */
    public static void showSelectDateDialog(Context mContext, long time, DatePicker.Callback callback) {
        showSelectDateTimeDialog(mContext, 10, time,false, callback);
    }
    /**
     * 选择 格式为 yyyy-MM-dd HH:mm
     * @param mContext
     * @param time
     * @param callback
     */
    public static void showSelectDateTimeDialog(Context mContext, long time, DatePicker.Callback callback) {
        showSelectDateTimeDialog(mContext, 10, time,true, callback);
    }
    private static void showSelectDateTimeDialog(Context mContext, int year, long time, boolean canShowPreciseTime, final DatePicker.Callback callback) {

        long cTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(cTime);
        calendar.add(Calendar.YEAR, -year);
        long beginTimestamp = calendar.getTimeInMillis();
        calendar.setTimeInMillis(cTime);
        calendar.add(Calendar.YEAR, year);
        long endTimestamp = calendar.getTimeInMillis();
        // 通过时间戳初始化日期，毫秒级别
        DatePicker mDatePicker = new DatePicker(mContext, new DatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                if (callback != null) {
                    callback.onTimeSelected(timestamp);
                }
            }
        }, beginTimestamp, endTimestamp);
        // 不允许点击屏幕或物理返回键关闭
        mDatePicker.setCancelable(false);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(canShowPreciseTime);
        // 不允许循环滚动
        mDatePicker.setScrollLoop(true);
        // 不允许滚动动画
        mDatePicker.setCanShowAnim(true);
        mDatePicker.show(time);
    }
}
