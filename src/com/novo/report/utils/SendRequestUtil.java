package com.novo.report.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

public class SendRequestUtil {
	public static String ajaxProxy(String url, String param1) {
		JSONObject node = new JSONObject();
		String randomLetters = RandomLetters();
		try {
			node.put("report_info", param1);
			node.put("report_key", randomLetters);
			node.put("report_value", MD5Util.MD5(randomLetters));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		// 使用POST方式向目的服务器发送请求
		URL connect;
		StringBuffer data = new StringBuffer();
		try {
			connect = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) connect.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");

			OutputStreamWriter paramout = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			paramout.write(node.toString());
			paramout.flush();

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				data.append(line);
			}
			paramout.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data.toString();
	}
	
	public static String RandomLetters(){  
        String s = "abcdefghijklmnopqrstuvwxyz";  
        char[] c = s.toCharArray();  
        Random random = new Random(); 
        String letter = "";
        for( int i = 0; i < 6; i ++) {  
        	letter+=c[random.nextInt(c.length)];  
        }
		return letter;  
    }
}
