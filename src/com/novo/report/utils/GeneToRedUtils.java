package com.novo.report.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import com.novo.report.interceptor.LoadOcacleListener;

public class GeneToRedUtils {
	
	/*public static String geneToRed(String str,String geneList) {
		String[] split = str.split(",");
		Set<String> set = new HashSet<String>();
		String[] split2 = geneList.split(",");
		for (String string : split2) {
			set.add(string.trim());
		}
		StringBuffer sb = new StringBuffer();
		for (String string : split) {
			if(set.contains(string.trim())) {
				sb.append("<w:r><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hAnsi=\"Times New Roman\" w:eastAsia=\"微软雅黑\" w:cs=\"Times New Roman\"/><w:color w:val=\"FF0000\"/><w:kern w:val=\"0\"/><w:sz w:val=\"16\"/><w:szCs w:val=\"16\"/><w:lang w:eastAsia=\"en-US\"/></w:rPr><w:t>"+string.trim()+",</w:t></w:r>");
			}else {
				sb.append("<w:r><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hAnsi=\"Times New Roman\" w:eastAsia=\"微软雅黑\" w:cs=\"Times New Roman\"/><w:color w:val=\"000000\"/><w:kern w:val=\"0\"/><w:sz w:val=\"16\"/><w:szCs w:val=\"16\"/><w:lang w:eastAsia=\"en-US\"/></w:rPr><w:t xml:space=\"preserve\">"+string.trim()+",</w:t></w:r>");
			}
		}
		String string = sb.toString();
		string = string.substring(0, string.lastIndexOf(","))+string.substring(string.lastIndexOf(",")+1, string.length());
		return string;
	}*/
	/*public static void main(String[] args) {
		Properties prop = new Properties();  
		
			InputStream inStream = GeneToRedUtils.class.getClassLoader().getResourceAsStream("user.properties");
			try {
				prop.load(inStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.err.println(prop.getProperty("cluster.host"));
			System.err.println(prop.getProperty("cluster.username"));
			System.err.println(prop.getProperty("cluster.password"));
			System.err.println();
			System.err.println(prop.getProperty("databases.host"));
			System.err.println(prop.getProperty("databases.username"));
			System.err.println(prop.getProperty("databases.password"));
	}*/
	
	public static void main(String[] args) {
		ArrayList<String> strList = new ArrayList<>();
		strList.add("序列1");
		strList.add("序列2");
		strList.add("序列3");
		strList.add("序列4");
		strList.add("序列5");

		// 测试：序列2与序列5对调
		int replaceNum1 = 1;
		int replaceNum2 = 4;

		strList.add(replaceNum1, strList.get(replaceNum2));
		strList.add(replaceNum2+1, strList.get(replaceNum1+1));

		strList.remove(replaceNum1+1);
		strList.remove(replaceNum2+1);

		System.out.println(strList.toString());
	}
}
