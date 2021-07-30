package com.novo.report.utils;

public class TextConversionUtil {
	
	/*
	 * 转义字符工具
	 * message  传入的字符串
	 * return  返回转义过的文本
	 * */
	public static String textConversion(String message) {
		if(message == null) {
			return "";
		}
		char[] content = new char[message.length()];
		message.getChars(0,message.length(), content, 0);
		StringBuffer sb = new StringBuffer();
		for(int i = 0;i<content.length;i++) {
			char c = content[i];
			switch(c) {
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				case '&':
					sb.append("&amp;");
					break;
				case '"':
					sb.append("&quot;");
					break;
				default:
					sb.append(c);
					break;
			}
		}
		return sb.toString();
	}
}
