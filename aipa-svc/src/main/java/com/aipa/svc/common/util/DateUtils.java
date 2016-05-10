package com.aipa.svc.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	public static final String formalPattern = "yyyy-MM-dd HH:mm:ss";
	public static final String simplePattern = "yyyyMMddHHmmss";
	public static String getDateStr(Date date,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
}
