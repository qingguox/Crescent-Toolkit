package io.github.qingguox.date;


import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author wangqingwei
 * Created on 2022-07-10
 */
public class DateConvertUtils {

    public static final String YYYYMMDD_DATE_FORMATE = "yyyyMMdd";
    public static final String YYYYMMDD_HHMMSS_DATA_FORMATE = "yyyy:MM:dd HH:mm:ss";


    /**
     * 根据时间戳ms获取yyyyMMdd格式的日期
     * @param timeStamp 时间戳
     * @return 日期
     */
    public static long getDateByTimeStamp(long timeStamp) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(YYYYMMDD_DATE_FORMATE);
        return Long.parseLong(formatter.print(timeStamp));
    }

    /**
     * 默认的转换
     * @param timeStamp 时间戳
     * @return 日期
     */
    public static String getDefaultDateByTimeStamp(long timeStamp) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(YYYYMMDD_HHMMSS_DATA_FORMATE);
        return formatter.print(timeStamp);
    }

    /**
     * 根据时间戳和指定pattern来转换日期
     * @param timeStamp 时间戳
     * @param pattern 方式
     * @return 日期
     */
    public static String getDateByTimeStampAndPattern(long timeStamp, String pattern) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        return formatter.print(timeStamp);
    }

    /**
     * 计算时间戳对应当天的开始时间
     * @param timeStamp 时间戳
     * @return 开始时间
     */
    public static long getDayStartTime(long timeStamp) {
        return new DateTime(timeStamp).withTimeAtStartOfDay().getMillis();
    }

    /**
     * 比较两个时间戳相差天数
     * @param firstTimeStamp 第一个时间戳
     * @param secondTimeStamp 第二个时间戳
     * @return 相差天数
     */
    public static long diffDaysBetweenTwoTimeStamp(long firstTimeStamp, long secondTimeStamp) {
        DateTime firstTime = new DateTime(firstTimeStamp).withTimeAtStartOfDay();
        DateTime secondTime = new DateTime(secondTimeStamp).withTimeAtStartOfDay();
        return Days.daysBetween(firstTime, secondTime).getDays();
    }
}
