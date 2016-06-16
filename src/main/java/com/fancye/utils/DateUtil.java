package com.fancye.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * http://dss16694.iteye.com/blog/1469251 <code>Calendar</code>的几个静态属性说明
 * http://blog.csdn.net/kingzone_2008/article/details/9256287
 * <code>java.util.Date</code>、<code>java.sql.Date</code>、
 * <code>Java.sql.Timestamp</code>以及Oracle中的日期和时间类型
 * 
 * @author FancyeBai
 * 
 */
public class DateUtil {

	private static Calendar c = Calendar.getInstance();

	public static void main(String[] args) throws ParseException {
		String s1 = "20150102 21:20:30";
		String s2 = "20150102 22:22:22";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		System.out.println(isSameDay(sdf.parse(s1), sdf.parse(s2)));
		System.out.println(sdf.format(getYesterday(new Date())));

		System.out.println(getFirstDayInCurrentWeek(new Date()));
		System.out.println(getFirstDayInCurrentMonth(new Date()));
		System.out.println(getFirstDayInCurrentYear(new Date()));
		
		System.out.println(getWeek(new Date()));
		System.out.println(getMonth(new Date()));
		System.out.println(getYear(new Date()));
	}

	/**
	 * 判断两个<code>Date</code>类型的日期是否是在同一周内
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean isSameWeek(Date d1, Date d2) {
		c.setTime(d1);
		int i1 = c.get(Calendar.WEEK_OF_YEAR);
		c.setTime(d2);
		int i2 = c.get(Calendar.WEEK_OF_YEAR);
		if (i1 == i2)
			return true;
		else
			return false;
	}

	/**
	 * 判断两个<code>Date</code>类型的日期是否是在同一天内
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean isSameDay(Date d1, Date d2) {
		c.setTime(d1);
		int i1 = c.get(Calendar.DAY_OF_YEAR);
		c.setTime(d2);
		int i2 = c.get(Calendar.DAY_OF_YEAR);
		if (i1 == i2)
			return true;
		else
			return false;
	}

	/**
	 * 获取指定日期的前一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getYesterday(Date date) {
		c.setTime(date);
		c.add(Calendar.DAY_OF_YEAR, -1);
		return c.getTime();
	}

	/**
	 * 获取指定日期的后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getTomorrow(Date date) {
		c.setTime(date);
		c.add(Calendar.DAY_OF_YEAR, 1);
		return c.getTime();
	}

	/**
	 * 获取指定日期的前后某一天，根据偏移量<code>offset</code>决定前后
	 * 
	 * @param date
	 * @param offset
	 *            负数往前数日期，正数往后数日期
	 * @return
	 */
	public static Date getOneDayByOffset(Date date, int offset) {
		c.setTime(date);
		c.add(Calendar.DAY_OF_YEAR, offset);
		return c.getTime();
	}

	/**
	 * 返回当前日期所在周内的第一天（按照日常习惯，即周一的日期）
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayInCurrentWeek(Date date) {
		c.setTime(date);
		return getOneDayByOffset(date, 2 - c.get(Calendar.DAY_OF_WEEK));
	}

	/**
	 * 返回当前日期所在月内的第一天的日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayInCurrentMonth(Date date) {
		c.setTime(date);
		return getOneDayByOffset(date, 1 - c.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * 返回当前日期所在年内的第一天的日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayInCurrentYear(Date date) {
		c.setTime(date);
		return getOneDayByOffset(date, 1 - c.get(Calendar.DAY_OF_YEAR));
	}

	/**
	 * <pre>
	 * 获取当前日期的星期，例如
	 * String[] week = {Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday}
	 * week[0] = "Sunday";
	 * @param date
	 * @return
	 */
	public static int getWeek(Date date) {
		c.setTime(date);
		int w = c.get(Calendar.DAY_OF_WEEK) - 1;

		return w < 0 ? 0 : w;
	}
	
	/**
	 * <pre>
	 * 获取当前日期的月份，例如
	 * String[] month = {January, February, march, April, may, June, July, August, September, October, November, December}
	 * month[0] = "January";
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date){
		c.setTime(date);
		int m = c.get(Calendar.MONTH);
		
		return m < 0 ? 0 : m;
	}
	
	/**
	 * 获取当前日期的年份
	 * @param date
	 * @return
	 */
	public static int getYear(Date date){
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}
}
