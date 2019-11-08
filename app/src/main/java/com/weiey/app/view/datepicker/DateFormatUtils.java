package com.weiey.app.view.datepicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @Author weiey
 * @Date 2019/2/26
 * @Description
 */
public class DateFormatUtils {

   private static final String DATE_FORMAT_PATTERN_YMD = "yyyy-MM-dd";
   private static final String DATE_FORMAT_PATTERN_YMD_HM = "yyyy-MM-dd HH:mm";

   /**
    * 时间戳转字符串
    *
    * @param timestamp     时间戳
    * @param isPreciseTime 是否包含时分
    * @return 格式化的日期字符串
    */
   public static String long2Str(long timestamp, boolean isPreciseTime) {
       return long2Str(timestamp, getFormatPattern(isPreciseTime));
   }

   private static String long2Str(long timestamp, String pattern) {
       return new SimpleDateFormat(pattern, Locale.CHINA).format(new Date(timestamp));
   }

   /**
    * 字符串转时间戳
    *
    * @param dateStr       日期字符串
    * @param isPreciseTime 是否包含时分
    * @return 时间戳
    */
   public static long str2Long(String dateStr, boolean isPreciseTime) {
       return str2Long(dateStr, getFormatPattern(isPreciseTime));
   }

   private static long str2Long(String dateStr, String pattern) {
       try {
           return new SimpleDateFormat(pattern, Locale.CHINA).parse(dateStr).getTime();
       } catch (Throwable ignored) {
       }
       return 0;
   }

   private static String getFormatPattern(boolean showSpecificTime) {
       if (showSpecificTime) {
           return DATE_FORMAT_PATTERN_YMD_HM;
       } else {
           return DATE_FORMAT_PATTERN_YMD;
       }
   }
    public static String getDateTimeText(long timestamp) {
        try{

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(timestamp);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int min = cal.get(Calendar.MINUTE);
//            int week = cal.get(Calendar.DAY_OF_WEEK);
            StringBuilder builder = new StringBuilder();
            builder.append(year).append("-");
            builder.append((month>9?month:"0"+month)).append("-");
            builder.append((day>9?day:"0"+day)).append(" ");
            builder.append((hour>9?hour:"0"+hour)).append(":").append((min>9?min:"0"+min));
            return builder.toString();
        }catch (Exception e){
            return "";
        }

    }
}
