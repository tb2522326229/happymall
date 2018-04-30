package com.mall.util;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * 时间类型和时间字符串之间的转换//joda-time
 */
public class DateTimeUtil {
	public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 根据传入的时间字符串和时间格式返回对应格式的时间
	 * @param dateTimeStr 时间的字符串
	 * @param formatStr 时间的格式
	 * @return 根据格式返回时间
	 */
    public static Date strToDate(String dateTimeStr,String formatStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    /**
     * 根据传入的时间和时间格式返回时间字符串
     * @param date 传入的时间
     * @param formatStr 时间的格式
     * @return 根据格式返回的时间字符串
     */
    public static String dateToStr(Date date,String formatStr){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatStr);
    }

    /**
	 * 根据传入的时间字符串返回对应格式的时间
	 * @param dateTimeStr 时间的字符串
	 * @return 根据内部定义的格式返回时间
	 */
    public static Date strToDate(String dateTimeStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    /**
     * 根据数据库中查询出的时间返回规定格式的时间字符串
     * @param date 传入的时间
     * @return 根据内部定义的格式返回的时间字符串
     */
    public static String dateToStr(Date date){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }
    
    public static void main(String[] args) {
        System.out.println(DateTimeUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateTimeUtil.strToDate("2010-01-01 11:11:11","yyyy-MM-dd HH:mm:ss"));

    }
}
