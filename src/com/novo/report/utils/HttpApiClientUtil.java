package com.novo.report.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpApiClientUtil {

	// 发送 GET 请求并解析 JSON 响应
	public static JsonObject sendGet(String urlStr, Map<String, String> queryParams, Map<String, String> headers) {
		try {
			// 拼接参数
			StringBuilder fullUrl = new StringBuilder(urlStr);
			if (queryParams != null && !queryParams.isEmpty()) {
				fullUrl.append("?");
				for (Map.Entry<String, String> entry : queryParams.entrySet()) {
					fullUrl.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
							.append("=")
							.append(URLEncoder.encode(entry.getValue(), "UTF-8"))
							.append("&");
				}
				fullUrl.setLength(fullUrl.length() - 1); // 去掉最后一个 &
			}

			URL url = new URL(fullUrl.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			// 设置请求头
			if (headers != null) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					conn.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}

			// 读取响应
			return readJsonResponse(conn);
		} catch (Exception e) {
			e.printStackTrace();
			return null;  // 返回 null 或者自己定义的错误处理
		}
	}

	// 发送 POST 请求并解析 JSON 响应
	public static JsonObject sendPost(String urlStr, String jsonBody, Map<String, String> headers) {
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

			// 设置额外的请求头
			if (headers != null) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					conn.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}

			// 发送请求体
			try (OutputStream os = conn.getOutputStream()) {
				os.write(jsonBody.getBytes("UTF-8"));
			}

			// 读取响应
			return readJsonResponse(conn);
		} catch (Exception e) {
			e.printStackTrace();
			return null;  // 返回 null 或者自己定义的错误处理
		}
	}

	// 读取响应并解析为 JSON
	private static JsonObject readJsonResponse(HttpURLConnection conn) throws IOException {
		int code = conn.getResponseCode();
		InputStream inputStream = (code == 200) ? conn.getInputStream() : conn.getErrorStream();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) response.append(line);

			// 使用 Gson 解析 JSON 字符串
			JsonParser parser = new JsonParser(); // Gson 2.7 版本的写法
			return parser.parse(response.toString()).getAsJsonObject();
		}
	}
}
