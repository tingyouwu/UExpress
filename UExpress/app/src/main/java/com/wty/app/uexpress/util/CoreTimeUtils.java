package com.wty.app.uexpress.util;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @Desc 日期处理相关工具类
 */
public class CoreTimeUtils {

    private static String[] WEEK = {
            "星期日",
            "星期一",
            "星期二",
            "星期三",
            "星期四",
            "星期五",
            "星期六"
    };

    public static final String DataFormat_yyyyMMdd_HHmmss0 = "yyyyMMddHHmmss";
    public static final String DataFormat_yyyyMMdd_HHmmss1 = "yyyy-MM-dd HH:mm:ss";
    public static final String DataFormat_yyyyMMdd_HHmmss2 = "yyyy年MM月dd日 HH:mm:ss";
    public static final String DataFormat_yyyyMMdd_HHmmss3 = "yyyy/MM/dd HH:mm:ss";
    public static final String DataFormat_yyyyMMdd_HHmm1 = "yyyy-MM-dd HH:mm";
    public static final String DataFormat_yyyyMMdd_HHmm2 = "yyyy年MM月dd日 HH:mm";
    public static final String DataFormat_yyyyMMdd_HHmm3 = "yyyy/MM/dd HH:mm";
    public static final String DataFormat_yyyyMMdd1 = "yyyy-MM-dd";
    public static final String DataFormat_yyyyMMdd2 = "yyyy年MM月dd日";
    public static final String DataFormat_yyyyMMdd3 = "yyyy/MM/dd";
    public static final String DataFormat_yyyyMM1 = "yyyy-MM";
    public static final String DataFormat_yyyyMM2 = "yyyy年MM月";
    public static final String DataFormat_yyyyMM3 = "yyyy/MM";
    public static final String DataFormat_yyyy1 = "yyyy";
    public static final String DataFormat_yyyy2 = "yyyy年";
    public static final String DataFormat_MMdd1 = "MM-dd";
    public static final String DataFormat_MMdd2 = "MM月dd日";
    public static final String DataFormat_MMdd3 = "MM/dd";
    public static final String DataFormat_MMdd_HHmm1 = "MM-dd HH:mm";
    public static final String DataFormat_MMdd_HHmm2 = "MM月dd日 HH:mm";
    public static final String DataFormat_MMdd_HHmm3 = "MM/dd HH:mm";
    public static final String DataFormat_MM1 = "MM";
    public static final String DataFormat_MM2 = "MM月";
    public static final String DataFormat_HHmm = "HH:mm";

    public static String getTime(String serviceTime) {
        return getTime(DataFormat_yyyyMMdd_HHmmss1, serviceTime);
    }

    public static String getTime(String dateFormat, String serviceTime) {
        if (TextUtils.isEmpty(serviceTime)) {
            return new SimpleDateFormat(dateFormat).format(new Date());
        } else {
            return transformLongToDate(dateFormat, Long.valueOf(serviceTime));
        }
    }

    /**
     * @Desc 比较两个日期
     * @return 比较结果
     * 0  相等
     * -1 date1比date2时间早
     * 1  date1比date2时间后
     */
    public static int compare_date(String date1, String date2) {
        return compare_date(date1, date2, DataFormat_yyyyMMdd_HHmmss1);
    }

    public static int compare_date_byMilliSecond(String date1, String date2){
        long t1 = date2Long(date1);
        long t2 = date2Long(date2);
        if (t1 > t2){
            return 1;
        }else if (t1 < t2){
            return -1;
        }else {
            return 0;
        }
    }

    /**
     * @Desc 比较两个日期
     * @return 比较结果
     * 0  相等
     * -1 date1比date2时间早
     * 1  date1比date2时间后
     */
    public static int compare_date(String date1, String date2, String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * @Desc 将时间转换成long型
     */
    public static long date2Long(String inVal) { // 此方法计算时间毫秒
        Date date = null; // 定义时间类型
        SimpleDateFormat inputFormat = new SimpleDateFormat(DataFormat_yyyyMMdd_HHmmss1);
        try {
            date = inputFormat.parse(inVal); // 将字符型转换成日期型
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date.getTime(); // 返回毫秒数
    }

    /**
     * @Desc 日期转换为"yyyy-MM-dd HH:mm" 格式
     **/
    public static String dateToYYYYMMddHHMM(String date) {
        DateFormat df = new SimpleDateFormat(DataFormat_yyyyMMdd_HHmmss1);
        try {
            return new SimpleDateFormat(DataFormat_yyyyMMdd_HHmm1).format(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    /**
     * @Desc 日期转换成 "yyyy-MM-dd" 的格式
     */
    public static String dateToYYYYMMdd(String date) {
        DateFormat df = new SimpleDateFormat(DataFormat_yyyyMMdd_HHmmss1);
        try {
            return new SimpleDateFormat(DataFormat_yyyyMMdd1).format(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    /**
     * 获取指定日期是星期几
     * 参数为null时表示获取当前日期是星期几
     */
    public static String dateToEEEE(String date) {
        Calendar calendar = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat(DataFormat_yyyyMMdd1);
        try {
            calendar.setTime(df.parse(date));
            int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayIndex < 1 || dayIndex > 8)
                return "";
            return WEEK[dayIndex - 1];
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    /**
     * 日期转换成 "MM-dd" 的格式
     */
    public static String dateToMMdd(String date) {
        DateFormat df = new SimpleDateFormat(DataFormat_yyyyMMdd_HHmmss1);
        try {
            return new SimpleDateFormat(DataFormat_MMdd1).format(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    /**
     * 日期转换成 "HH:mm" 的格式
     */
    public static String dateToHHmm(String date) {
        DateFormat df = new SimpleDateFormat(DataFormat_yyyyMMdd_HHmmss1);
        try {
            return new SimpleDateFormat(DataFormat_HHmm).format(df
                    .parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    /**
     * 日期转换成 "MM月" 的格式
     */
    public static String dateToMM(String date) {
        String dateFormat = DataFormat_MM2;
        DateFormat df = new SimpleDateFormat(DataFormat_yyyyMMdd_HHmmss1);
        try {
            return new SimpleDateFormat(dateFormat).format(df.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
            return date;
        }
    }

    /**
     * 日期转换成 "yyyy年MM月" 的格式
     */
    public static String dateToYYYYMM(String date) {
        String dateFormat = DataFormat_yyyyMM2;
        DateFormat df = new SimpleDateFormat(DataFormat_yyyyMMdd_HHmmss1);
        try {
            return new SimpleDateFormat(dateFormat).format(df.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
            return date;
        }
    }


    public static String parseNoticeDate(String createTime, String originalSDF) {

        String df_result_today = DataFormat_HHmm;
        String df_result_thisYear = DataFormat_MMdd_HHmm2;
        String df_result_otherYear = DataFormat_yyyyMMdd1;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(originalSDF);
            Date createDate = sdf.parse(createTime);

            String today = getTime(DataFormat_yyyyMMdd1);
            String createDay = new SimpleDateFormat(DataFormat_yyyyMMdd1).format(createDate);

            if (today.equals(createDay)) {
                //ret = "今天"
                return new SimpleDateFormat(df_result_today).format(createDate);
            }

            String createYear = new SimpleDateFormat(DataFormat_yyyy1).format(createDate);
            String nowYear = new SimpleDateFormat(DataFormat_yyyy1).format(new Date());
            if (createYear.equals(nowYear)) {
                // ret= "本年";
                return new SimpleDateFormat(df_result_thisYear).format(createDate);
            } else {
                // ret= "不是本年";
                return new SimpleDateFormat(df_result_otherYear).format(createDate);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String parseDate(String createTime, String originalSDF) {

        String df_result_lastYesterday = DataFormat_MMdd1;
        String df_result_lastYear =DataFormat_yyyyMMdd1;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(originalSDF);

            Date createDate = sdf.parse(createTime);
            String createYear = new SimpleDateFormat(DataFormat_yyyy1).format(createDate);
            String nowYear = new SimpleDateFormat(DataFormat_yyyy1).format(new Date());
            if (createYear.equals(nowYear)) {
                // ret= "本年";
                return new SimpleDateFormat(df_result_lastYesterday).format(createDate);
            } else {
                // ret= "不是本年";
                return new SimpleDateFormat(df_result_lastYear).format(createDate);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Desc 返回 MM月dd日  或者yyyy年MM月dd日
     */
    public static String parseOfficeDate(String createTime, String originalSDF) {

        try {
            String df_result_lastYesterday = DataFormat_MMdd2;
            String df_result_lastYear = DataFormat_yyyyMMdd2;
            SimpleDateFormat sdf = new SimpleDateFormat(originalSDF);

            Date createDate = sdf.parse(createTime);
            String createYear = new SimpleDateFormat(DataFormat_yyyy1).format(createDate);
            String nowYear = new SimpleDateFormat(DataFormat_yyyy1).format(new Date());
            if (createYear.equals(nowYear)) {
                // ret= "本年";
                return new SimpleDateFormat(df_result_lastYesterday).format(createDate);
            } else {
                // ret= "不是本年";
                return new SimpleDateFormat(df_result_lastYear).format(createDate);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Desc  返回  HH:mm 或者 昨天 HH:mm 或者 MM月dd日 HH:mm  或者 yyyy年MM月dd日 HH:mm
     */
    public static String parseDate(String createTime) {

        String df_result_today = DataFormat_HHmm;
        String df_result_yesterday = "昨天 "+DataFormat_HHmm;
        String df_result_lastYesterday = DataFormat_MMdd_HHmm2;
        String df_result_lastYear = DataFormat_yyyyMMdd_HHmm2;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DataFormat_yyyyMMdd_HHmmss1);

            Date createDate = sdf.parse(createTime);

            long create = createDate.getTime();
            Calendar now = Calendar.getInstance();
            long ms = 1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600
                    + now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND));// 毫秒数
            long ms_now = now.getTimeInMillis();
            if (ms_now - create < ms) {
                // ret = "今天";
                return new SimpleDateFormat(df_result_today).format(createDate);
            } else if (ms_now - create < (ms + 24 * 3600 * 1000)) {
                // ret = "昨天";
                return new SimpleDateFormat(df_result_yesterday)
                        .format(createDate);
            } else {
                String createYear = new SimpleDateFormat(DataFormat_yyyy1)
                        .format(createDate);
                String nowYear = new SimpleDateFormat(DataFormat_yyyy1)
                        .format(new Date());
                if (createYear.equals(nowYear)) {
                    // ret= "本年";
                    return new SimpleDateFormat(df_result_lastYesterday)
                            .format(createDate);
                } else {
                    // ret= "不是本年";
                    return new SimpleDateFormat(df_result_lastYear)
                            .format(createDate);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Desc  返回  HH:mm 或者 MM月dd日 或者 昨天
     */
    public static String parseRecentlyDate(String createTime) {

        String df_result_today = DataFormat_HHmm;
        String df_result_other = DataFormat_MMdd2;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DataFormat_yyyyMMdd_HHmmss1);

            Date createDate = sdf.parse(createTime);

            long create = createDate.getTime();
            Calendar now = Calendar.getInstance();
            long ms = 1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600
                    + now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND));// 毫秒数
            long ms_now = now.getTimeInMillis();
            if (ms_now - create < ms) {
                return new SimpleDateFormat(df_result_today).format(createDate);
            } else if (ms_now - create < (ms + 24 * 3600 * 1000)) {
                return "昨天";
            } else {
                return new SimpleDateFormat(df_result_other).format(createDate);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Desc  返回  HH:mm 或者 MM月dd日 或者 昨天 HH:mm
     */
    public static String parseRecentlyDate2(String createTime) {

        if (TextUtils.isEmpty(createTime)) return "";

        String df_result_today = DataFormat_HHmm;
        String df_result_other = DataFormat_MMdd2;
        String df_result_yesterday = "昨天 "+DataFormat_HHmm;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DataFormat_yyyyMMdd_HHmmss1);

            Date createDate = sdf.parse(createTime);

            long create = createDate.getTime();
            Calendar now = Calendar.getInstance();
            long ms = 1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600
                    + now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND));// 毫秒数
            long ms_now = now.getTimeInMillis();
            if (ms_now - create < ms) {
                return new SimpleDateFormat(df_result_today).format(createDate);
            } else if (ms_now - create < (ms + 24 * 3600 * 1000)) {
                return new SimpleDateFormat(df_result_yesterday)
                        .format(createDate);
            } else {
                return new SimpleDateFormat(df_result_other).format(createDate);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Calendar get1980() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DataFormat_yyyyMMdd1);
        try {
            Date date = sdf.parse("1980-01-01");
            c.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c;
    }

    public static String dateToFormat(String inFormat, String outFormat, String dateStr) {
        Calendar calendar = dateToCalendar(dateStr, inFormat);
        return calendarToDate(calendar, outFormat);
    }

    public static Calendar dateStrToCalendar(String dateStr) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DataFormat_yyyyMMdd1);
        try {
            Date date = sdf.parse(dateStr);
            c.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
            c.setTime(new Date());
        }
        return c;
    }

    public static Calendar dateTimeStrToCalendar(String dateTimeStr) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DataFormat_yyyyMMdd_HHmm1);
        try {
            Date date = sdf.parse(dateTimeStr);
            c.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
            c.setTime(new Date());
        }
        return c;
    }

    public static Calendar dateToCalendar(String dateStr) {
        return dateToCalendar(dateStr, DataFormat_yyyyMMdd1);
    }

    public static Calendar dateToCalendar(String dateStr, String format) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date = sdf.parse(dateStr);
            c.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
            c.setTime(new Date());
        }
        return c;
    }

    public static String calendarToDate(Calendar calendar) {
        return calendarToDate(calendar, DataFormat_yyyyMMdd1);
    }

    public static String calendarToDate(Calendar calendar, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static String calendarToDateTime(Calendar calendar) {
        return calendarToDate(calendar, DataFormat_yyyyMMdd_HHmm1);
    }

    /**
     * 把毫秒转化成日期
     * @param dateFormat(日期格式)
     * @param millSec(毫秒数)
     */
    public static String transformLongToDate(String dateFormat, Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    /**
     * 日期转换成 任意 的格式
     *
     * @param date
     * @return
     */
    public static String dateToFormat(String dateFormat, String date) {
        DateFormat df = new SimpleDateFormat(DataFormat_yyyyMMdd_HHmmss1);
        try {
            return new SimpleDateFormat(dateFormat).format(df.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
            return date;
        }
    }

    public static String dateToFormat(String dateFormat, Date date) {
        String result = new SimpleDateFormat(dateFormat).format(date);
        return result;
    }

    /**
     * 获取当月第一天日期
     * @param
     * @param
     * @return px
     */
    public static String dateGetMonthFirst(String dateStr) {

        try {
            if (TextUtils.isEmpty(dateStr)) return "";
            //把yyyy-MM-dd HH:mm:ss  -> yyyy-MM-dd
            Date date = new SimpleDateFormat(DataFormat_yyyyMMdd_HHmmss1).parse(dateStr);
            String dayStr = new SimpleDateFormat(DataFormat_yyyyMM1).format(date);
            //把yyyy-MM -> yyyy-MM-dd
            date = new SimpleDateFormat(DataFormat_yyyyMM1).parse(dayStr);
            return new SimpleDateFormat(DataFormat_yyyyMMdd_HHmmss1).format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return dateStr;
        }
    }

    /**
     * 获取当前月
     * @return
     */
    public static String getMonth(String source) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(DataFormat_yyyyMMdd_HHmmss1);
            Calendar calendar = new GregorianCalendar();
            Date theDate = df.parse(source);
            calendar.setTime(theDate);
            df = new SimpleDateFormat(DataFormat_yyyyMM1);
            return df.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 当月第一天
     * @return
     */
    public static String getFirstDay(String source) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(DataFormat_yyyyMMdd_HHmmss1);
            Calendar calendar = new GregorianCalendar();
            Date theDate = df.parse(source);
            calendar.setTime(theDate);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            return df.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 当月最后一天
     * @return
     */
    public static String getLastDay(String source) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(DataFormat_yyyyMMdd_HHmmss1);
            Calendar calendar = new GregorianCalendar();
            Date theDate = df.parse(source);
            calendar.setTime(theDate);
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            return df.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 使用预设格式提取字符串日期
     * @param strDate 日期字符串
     * @return
     */
    public static Date parse(String strDate) {
        return parse(strDate, DataFormat_yyyyMMdd_HHmmss1);
    }

    /**
     * 使用用户格式提取字符串日期
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */
    public static Date parse(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用用户格式提取字符串日期,输出字符串日期
     * @param strDate
     * @param pattern
     * @return
     */
    public static String getDateFormate(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            Date date = df.parse(strDate);
            return df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取系统当前时间
     * @return
     */
    public static String getNowTime() {
        SimpleDateFormat df = new SimpleDateFormat(DataFormat_yyyyMMdd_HHmmss1);
        return df.format(new Date());
    }

    /**
     * 获取系统当前时间 - 带毫秒-用于测试
     * @return
     */
    public static String getNowTimeTest() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss | SSS ");
        return df.format(new Date());
    }

    /**
     * 将指定的日期转换成Unix时间戳
     * @param date
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp(String date) {
        return dateToUnixTimestamp(date,DataFormat_yyyyMMdd_HHmmss1);
    }

    /**
     * 将指定的日期转换成Unix时间戳
     * @param date
     * @param dateFormat
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp(String date, String dateFormat) {
        long timestamp = 0;
        try {
            timestamp = new SimpleDateFormat(dateFormat).parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    /**
     * 将当前日期转换成Unix时间戳
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp() {
        long timestamp = new Date().getTime();
        return timestamp;
    }

    /**
     * 将Unix时间戳转换成日期
     * @param timestamp
     * @return String 日期字符串
     */
    public static String unixTimestampToDate(long timestamp) {
        SimpleDateFormat sd = new SimpleDateFormat(DataFormat_yyyyMMdd_HHmmss0);
        sd.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sd.format(new Date(timestamp));
    }

    /**
     * @Desc 获取21天后的时间
     **/
    public static String get21DaysAfter() {
        return getDateAfter(21);
    }

    /**
     * @Desc 获取n天后的时间
     * @param count n天
     **/
    public static String getDateAfter(int count) {
        long t_now = System.currentTimeMillis();
        long dayTimes = 1000 * 60 * 60 * 24 * count;
        long t_after = t_now + dayTimes;//同一时间21日后的时间
        String ts_after = transformLongToDate(DataFormat_yyyyMMdd_HHmmss1, t_after);
        return ts_after;
    }
}