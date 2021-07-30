package com.novo.report.utils;

import java.util.List;

public class StringJoinUtil {
	
	public static String getStrings(List<String> s) {
		String strings="";
		if(s!=null){
			for (int i = 0; i < s.size(); i++) {
				if(i==s.size()-1){
					strings+=s.get(i);
				}else{
					strings+=s.get(i)+",";
				}
			}
		}
		return strings;
	}
}
