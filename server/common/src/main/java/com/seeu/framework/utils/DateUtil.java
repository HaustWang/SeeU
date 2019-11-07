package com.seeu.framework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by LL on 2018/5/23.
 */
public class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat SDF_DAY = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat SDF_TRIM = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final SimpleDateFormat SDF_TRIM_D = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat SDF_TRIM_M = new SimpleDateFormat("yyyyMMddHHmm");

    /**
     * 返回 yyyy-MM-dd HH:mm:ss 日期格式
     */
    public static String getYYYYTime() {
        return getYYYYTime(new Date());
    }

    public static String getYYYYTime(Date currentTime) {
        return SDF.format(currentTime);
    }

    public static String getYYYYTime(Date currrentime, TimeZone timeZone) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(timeZone);
        return sdf.format(currrentime);
    }

    public static String getYMDTime(Date currentTime) {
        return SDF_DAY.format(currentTime);
    }

    public static Date getYYYYTime(String currentTime) throws ParseException {
        return SDF.parse(currentTime);
    }

    public static Date getByDay(String day) throws ParseException {
        return SDF_DAY.parse(day);
    }

    public static String getCurrentTime() {
        Date currentTime = new Date();

        return SDF_TRIM.format(currentTime);
    }

    public static String getCurrentMinute() {
        return SDF_TRIM_M.format(new Date());
    }


    public static String getEndTime(Date date) {

        String dateString = SDF_DAY.format(date);
        dateString += " 23:59:59";
        return dateString;
    }

    public static String getCurrentDay() {
        Date currentTime = new Date();
        return SDF_DAY.format(currentTime);
    }

    public static String getNowDate() {
        Date currentTime = new Date();
        return SDF_TRIM_D.format(currentTime);
    }

    public static Date getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    public static Date getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }

    public static long getInterSecondsFromNow(Date date) {
        Date currentTime = new Date();
        long millSec1 = currentTime.getTime();
        long millSec2 = date.getTime();
        return Math.abs(millSec1 - millSec2) / 1000;
    }

    public static long getInterSecondsFromTodayStart() {
        return getInterSecondsFromNow(getStartTime());
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2
            .get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2
            .get(Calendar.DAY_OF_YEAR));
    }

    /**
     * 计算两个日期之间相差的秒数
     *
     * @param date1 // * @param date2
     */
    public static long secsBetween(Date date1) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date);
        long time2 = cal.getTimeInMillis();
        return (time2 - time1) / 1000;
    }

    public static long secsBetweenAbs(Date date1) {
        return Math.abs(secsBetween(date1));
    }

    /**
     * 计算 totalDate 到  nowDate 相差的日期
     *
     * @param unit ChronoUnit.DAYS
     */
    public static long until(LocalDate totalDate, LocalDate nowDate, TemporalUnit unit) {
        return nowDate.until(totalDate, unit);
    }

    public static long betweenDateToNow(String date, TemporalUnit unit) {
        LocalDate totalDate = LocalDate.parse(date);
        LocalDate nowDate = LocalDate.now();
        return DateUtil.until(nowDate, totalDate, unit);
    }

    public static long betweenDateForNow(String date, TemporalUnit unit) {
        LocalDate totalDate = LocalDate.parse(date);
        LocalDate nowDate = LocalDate.now();
        return DateUtil.until(totalDate, nowDate, unit);
    }

    /**
     * 两个日期间的天数
     */
    public static long betweenDateToDate(Date firstDt, Date secentDt) {
        String firstDt_Str = SDF_DAY.format(firstDt);
        String secentDt_Str = SDF_DAY.format(secentDt);
        return betweenDateToDate(firstDt_Str, secentDt_Str);
    }

    public static long betweenDateToDate(String firstDt_str, String secentDt_str) {
        LocalDate localDate1 = LocalDate.parse(firstDt_str);
        LocalDate localDate2 = LocalDate.parse(secentDt_str);
        return localDate2.until(localDate1, ChronoUnit.DAYS);
    }

    /**
     * @param unit ChronoUnit.DAYS
     */
    public static Date addDay(Date date, long day, TemporalUnit unit) {
        Instant instant = date.toInstant();
        instant = instant.plus(day, unit);
        return Date.from(instant);
    }

    public static Instant addDayInstant(Date date, long day, TemporalUnit unit) {
        Instant instant = date.toInstant();
        return instant.plus(day, unit);
    }

    /**
     * 判断日期是否为当天
     */
    public static boolean isNowDate(long time, String timeZone) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        String param = sdf.format(date);
        String now = sdf.format(new Date());
        if (param.equals(now)) {
            return true;
        }
        return false;
    }

    /**
     * 根据日期获取当年第几周
     */
    public static int getWeekDate(String date) {
        int week = 0;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.setTime(SDF_DAY.parse(date));
            week = calendar.get(Calendar.WEEK_OF_YEAR);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return week;
    }

    /**
     * 获取当前日期下周一日期
     */
    public static String getNextMonday() {
        Calendar cd = Calendar.getInstance();
        Date date = new Date();
        cd.setTime(date);
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        int nextMondayOffset = dayOfWeek == 1 ? 1 : 9 - dayOfWeek;
        cd.add(Calendar.DAY_OF_MONTH, nextMondayOffset);
        return SDF_DAY.format(cd.getTime());
    }

    /**
     * 获取当前时间周一的日期
     */
    public static String getMondayDate() {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (dayWeek == 1) {
            dayWeek = 8;
        }
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);
        Date mondayDate = cal.getTime();
        String weekBegin = SDF_DAY.format(mondayDate);
        return weekBegin;
    }

    /**
     * 获取现在日期 +自定义天数的日期
     */
    public static String getDate(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, days);
        Date time = cal.getTime();
        String dateString = SDF_DAY.format(time);
        return dateString;
    }

    /**
     * String日期转时间戳
     */
    public static Long getCurrentTimeMillis(String time) {
        Date date = null;
        try {
            date = SDF.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 时间戳转字符串
     */
    public static String getDateString(long timeMillis, String format) {
        Date date = new Date(timeMillis);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Calendar getTimeOfZone(Date time, String zone) {
        TimeZone timeZone = TimeZone.getTimeZone(zone);
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(time);
        return calendar;
    }

    public static Calendar getCurrTimeOfZone(String zone) {
        TimeZone timeZone = TimeZone.getTimeZone(zone);
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(new Date());
        return calendar;
    }

    public static int getWeekNum() {
        Calendar cal = Calendar.getInstance();
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (0 == week) {
            week = 7;
        }
        return week;
    }

}
