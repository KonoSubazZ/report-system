package com.novo.report.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//工具类的构造方法一般是私有的
	private DateUtil(){}
	
	//获取系统时间 时间格式
	public static String getSystemTime(){
		return sdf.format(new Date());
	}
}
