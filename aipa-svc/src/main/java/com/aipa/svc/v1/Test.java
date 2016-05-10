package com.aipa.svc.v1;

import java.util.Calendar;
import java.util.Date;

public class Test {

	
	public static void main(String[] args) {
		Long id = 11111l;
		Long me = 11111l;
		
		Integer ee = 1111;
		Integer aa = 1111;
		System.out.println(id == me);
		System.out.println(ee == aa);
		
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR,1);
		System.out.println(cal.getTime());
	}
}
