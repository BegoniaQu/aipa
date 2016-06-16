package com.aipa.svc.v1;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test {

	public static Date getFullDate (String partTime){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(date);
		dateStr = dateStr + " "+ partTime;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			return sdf1.parse(dateStr);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}


	
	
	
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
		
		
		String pic = "123.gif";
		String preffix = pic.substring(0,pic.lastIndexOf("."));
		System.out.println(preffix);
		String extensionName = pic.substring(pic.lastIndexOf(".") + 1); 	// 获取图片的扩展名
		System.out.println(extensionName);
		
		Date d = new Date();
		System.out.println(d);
		long then = d.getTime()/1000L;
		System.out.println(then);
		long f = Long.parseLong(String.valueOf(then)+"000");
		System.out.println(new Date(f));
		
		
		String host = "http://www.baidu.com/{user_id}/xxx";
		host = host.replaceAll("\\{user_id\\}", "quyang");
		System.out.println(host);
		
		double amount = 4.1;
		double computed = amount * 0.135 * 100;
		System.out.println(computed);
		BigDecimal bd = new BigDecimal(computed).setScale(0, BigDecimal.ROUND_HALF_UP);
		System.out.println(bd);
		//DecimalFormat df=new DecimalFormat("#.##");   
		//System.out.println(df.format(computed));
		
		 double income = 6.0 * 0.135 * 100; //分为单位
		 System.out.println(income);
	      BigDecimal res = new BigDecimal(income).setScale(0, BigDecimal.ROUND_HALF_UP); //四舍五入取整
	      System.out.println(res);
	      
	      int result = getFullDate("17:00:00").compareTo(new Date());
		System.out.println(result);
		
		//
		double realPay = 3.0;
		double orderAmount = 5.0;
		BigDecimal b1 = new BigDecimal(String.valueOf(realPay));
	    BigDecimal b2 = new BigDecimal(String.valueOf(orderAmount));
	    double v = b1.divide(b2,2,BigDecimal.ROUND_HALF_UP).doubleValue();
	    System.out.println("------------"+v*1.5);
	    
	    doubleMultiply(1.5,3,0.5);
	}
	
	public static void doubleMultiply(double a,double b,double c){
		BigDecimal a1 = new BigDecimal(String.valueOf(a));
		BigDecimal b1 = new BigDecimal(String.valueOf(b));
		BigDecimal c1 = new BigDecimal(String.valueOf(c));
		BigDecimal v = a1.multiply(b1).multiply(c1);
		System.out.println(v.doubleValue());
		
		System.out.println(a1.subtract(b1).doubleValue());
		
		System.out.println(a+c);
	}
}
