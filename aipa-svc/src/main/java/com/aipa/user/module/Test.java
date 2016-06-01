package com.aipa.user.module;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test {

	
	public static final String formalPattern = "yyyyMMdd HH:mm:ss";
	public static final String datePattern = "yyyyMMdd";
	public static final String timePattern = "HH:mm";
	

	public static Date addDay(Date date,int dayNum){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR,dayNum);
		return cal.getTime();
	}

	public static Date stringToDate(String arg0,String pattern){
		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(arg0);
		} catch (Exception e) {
			throw new RuntimeException("param error");
		}
	}

	public static void main(String [] args){
		Date date = stringToDate("20160519",datePattern);
		System.out.println(date);
		Date today = new Date();
		System.out.println(today);
		
	}

	public static String dateToString(Date date,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	public static String convertDayToWeekDay(String date_str) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date day = sdf.parse(date_str);
		return convertDayToWeekDay(day);
	}

	public static String convertDayToWeekDay(Date day) throws Exception{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(day);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		return Integer.toString((dayOfWeek + 5) % 7 + 1);
	}

	public static Date getTimeInDay(String timeInDay) throws Exception{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		if(timeInDay.equals("24:00:00")){
			timeInDay = "23:59:59";
		}
		Date date = simpleDateFormat.parse(timeInDay);
		Calendar dayTime = Calendar.getInstance();
		dayTime.clear();
		dayTime.setTime(date);

		Date today = new Date();
		Calendar time = Calendar.getInstance();
		time.clear();
		time.setTime(today);

		dayTime.set(Calendar.YEAR, time.get(Calendar.YEAR));
		dayTime.set(Calendar.MONTH, time.get(Calendar.MONTH));
		dayTime.set(Calendar.DATE, time.get(Calendar.DATE));
		return dayTime.getTime();
	}
}
