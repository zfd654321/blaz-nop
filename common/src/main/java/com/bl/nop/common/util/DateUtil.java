package com.bl.nop.common.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
	            
	/**
	   * 获取现在时间
	   * 
	   * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
	   */
	public static String getStringNowDate() {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String dateString = formatter.format(currentTime);
	   return dateString;
	}
	public static String getStringNowDate0() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date currentTime = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	public static String getStringNowDate23() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		Date currentTime = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	
	/**
	   * 获取现在时间
	   * 
	   * @return 返回短时间字符串格式yyyy-MM-dd
	   */
	public static String getStringDateShort() {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	   String dateString = formatter.format(currentTime);
	   return dateString;
	}
	public static String getStringDateShortYMD() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	public static String getStringDateShortYM() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	/**
	   * 获取时间 小时:分;秒 HH:mm:ss
	   * 
	   * @return
	   */
	public static String getStringNowTimeShort() {
	   SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	   Date currentTime = new Date();
	   String dateString = formatter.format(currentTime);
	   return dateString;
	}
	/**
	   * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
	   * 
	   * @param strDate
	   * @return
	   */
	public static Date strToDateLong(String strDate) {
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   ParsePosition pos = new ParsePosition(0);
	   Date strtodate = formatter.parse(strDate, pos);
	   return strtodate;
	}
	/**
	   * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
	   * 
	   * @param dateDate
	   * @return
	   */
	public static String dateToStrLong(Date dateDate) {
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String dateString = formatter.format(dateDate);
	   return dateString;
	}
	public static String dateToStrLongYMDHMS(Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(dateDate);
		return dateString;
	}
	/**
	   * 将短时间格式时间转换为字符串 yyyy-MM-dd
	   * 
	   * @param dateDate
	   * @param k
	   * @return
	   */
	public static String dateToStrShort(Date dateDate) {
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	   String dateString = formatter.format(dateDate);
	   return dateString;
	}
	
	public static String formatDate(Date dateDate, String formater) {
		SimpleDateFormat format = new SimpleDateFormat(formater);
		String dateString = format.format(dateDate);
		return dateString;
	}
	/**
	   * 将短时间格式字符串转换为时间 yyyy-MM-dd 
	   * 
	   * @param strDate
	   * @return
	   */
	public static Date strToDateShort(String strDate) {
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	   ParsePosition pos = new ParsePosition(0);
	   Date strtodate = formatter.parse(strDate, pos);
	   return strtodate;
	}
	
	public static Date strToDateLong(String strDate, String formater) {
		SimpleDateFormat formatter = new SimpleDateFormat(formater);
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}
	/**
	   * 得到现在时间
	   * 
	   * @return
	   */
	public static Date getNow() {
	   Date currentTime = new Date();
	   return currentTime;
	}
	/**
	   * 得到现在时间
	   * 
	   * @return 字符串 yyyyMMdd HHmmss
	   */
	public static String getStringToday() {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss");
	   String dateString = formatter.format(currentTime);
	   return dateString;
	}
	public static String getStringYMDHMS() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	/**
	   * 得到现在小时
	   */
	public static String getHour() {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String dateString = formatter.format(currentTime);
	   String hour;
	   hour = dateString.substring(11, 13);
	   return hour;
	}
	/**
	   * 得到现在分钟
	   * 
	   * @return
	   */
	public static String getMinute() {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String dateString = formatter.format(currentTime);
	   String min;
	   min = dateString.substring(14, 16);
	   return min;
	}

	 /**
	  * 判断是否润年
	  * 
	  * @param ddate
	  * @return
	  */
	 public static boolean isLeapYear(Date d) {
		 /**
		   * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
		   * 3.能被4整除同时能被100整除则不是闰年
		   */
		 GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		 gc.setTime(d);
		 int year = gc.get(Calendar.YEAR);
		 if ((year % 400) == 0)
		   return true;
		 else if ((year % 4) == 0) {
		   if ((year % 100) == 0)
		      return false;
		   else
		      return true;
		 } else
		    return false;
	 }


	 /**
	  * 判断二个时间是否在同一个周
	  * 
	  * @param date1
	  * @param date2
	  * @return
	  */
	 public static boolean isSameWeekDates(Date date1, Date date2) {
		 Calendar cal1 = Calendar.getInstance();
		 Calendar cal2 = Calendar.getInstance();
		 cal1.setTime(date1);
		 cal2.setTime(date2);
		 int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		 if (0 == subYear) {
		   if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
		    return true;
		 } else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
		   // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
		   if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
		    return true;
		 } else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
		   if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
		    return true;
		 }
		 return false;
	 }


	 /**
	  * 根据一个日期，返回是星期几的字符串
	  * 
	  * @param sdate
	  * @return
	  */
	 public static int getWeek(Date date) {
	 	// 再转换为时间
	 	Calendar c = Calendar.getInstance();
	 	c.setTime(date);
	 	int week = c.get(Calendar.DAY_OF_WEEK);
	 	// week中存的就是星期几了，其范围 1~7
	 	// 1=星期日 7=星期六，其他类推
	 	return week;
	 }
	 
	 public static String getWeekStr(Date sdate){
		Calendar c = Calendar.getInstance();
		c.setTime(sdate);
	 	return new SimpleDateFormat("EEE").format(c.getTime());
	 }
	 
	 public static Date addSecond(Date date, int second) {
		 Calendar c = Calendar.getInstance();
		 c.setTime(date);
		 c.add(Calendar.SECOND, second);
		 return c.getTime();
	 }
	 
	 public static Date addMinute(Date date, int minute) {
		 Calendar c = Calendar.getInstance();
		 c.setTime(date);
		 c.add(Calendar.MINUTE, minute);
		 return c.getTime();
	 }
	 
	 public static Date addDay(Date date, int day) {
		 Calendar c = Calendar.getInstance();
		 c.setTime(date);
		 c.add(Calendar.DAY_OF_MONTH, day);
		 return c.getTime();
	 }
	 
	 public static Date addHour(Date date, int hour) {
		 Calendar c = Calendar.getInstance();
		 c.setTime(date);
		 c.add(Calendar.HOUR, hour);
		 return c.getTime();
	 }
	 public static Date addMonth(Date date, int month) {
		 Calendar c = Calendar.getInstance();
		 c.setTime(date);
		 c.add(Calendar.MONTH, month);
		 return c.getTime();
	 }
	 public static int daysxiangcha(String dateStr1, String dateStr2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dateStr1 = sdf.format(sdf2.parse(dateStr1));
			dateStr2 = sdf.format(sdf2.parse(dateStr2));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int year1 = Integer.parseInt(dateStr1.substring(0, 4));
		int month1 = Integer.parseInt(dateStr1.substring(4, 6));
		int day1 = Integer.parseInt(dateStr1.substring(6, 8));
		int year2 = Integer.parseInt(dateStr2.substring(0, 4));
		int month2 = Integer.parseInt(dateStr2.substring(4, 6));
		int day2 = Integer.parseInt(dateStr2.substring(6, 8));
		Calendar c1 = Calendar.getInstance();
		c1.set(Calendar.YEAR, year1);
		c1.set(Calendar.MONTH, month1 - 1);
		c1.set(Calendar.DAY_OF_MONTH, day1);
		Calendar c2 = Calendar.getInstance();
		c2.set(Calendar.YEAR, year2);
		c2.set(Calendar.MONTH, month2 - 1);
		c2.set(Calendar.DAY_OF_MONTH, day2);
		long mills = c1.getTimeInMillis() > c2.getTimeInMillis() ? c1.getTimeInMillis() - c2.getTimeInMillis()
				: c2.getTimeInMillis() - c1.getTimeInMillis();
		return (int) (mills / 1000 / 3600 / 24);
	}
	 /**
	 * 得到当前的时间是人几点
	 * @param nowTime
	 * @return
	 */
    public static int getHourOfDay(Date nowTime){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(nowTime);
    	 int hour=calendar.get(Calendar.HOUR_OF_DAY);  
    	 return hour;
	}
    /**
	 * 得到上个月的第一天
	 * @return
	 */
	public static Date getBeforeMonthFirstDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH,1);
		date = calendar.getTime();
		return date;
	}
	/**
	 * 得到上个月的最后一天
	 * @return
	 */
	public static Date getBeforeMonthLastDay(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1); 
		calendar.add(Calendar.DATE, -1);
		date = calendar.getTime();
		return date;
	}
	/**
	 * 比较两个日期相隔多少天
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDaySpace(Date date1,Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
       int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
        
        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 > year2) {
        	return -1;
        }
        if(year1 != year2)   //同一年
        {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)    //闰年            
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }
            
            return timeDistance + (day2-day1) ;
        }
        else    //不同年
        {
            System.out.println("判断day2 - day1 : " + (day2-day1));
            return day2-day1;
        }
    }
}
