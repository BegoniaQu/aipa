package com.aipa.user.module.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Test {

	public static void main(String[] args) {
		long current = System.currentTimeMillis();
		System.out.println(current);
		System.out.println(new Date(current));
		long then = current/1000;
		System.out.println(then);
		System.out.println(then%10000000000L);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(new Date(then*1000));
	}

}
