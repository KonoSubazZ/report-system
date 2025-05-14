package com.novo.report.utils;

import java.util.*;

public class MapUtils {

	/**
	 * 从 map 中获取指定 key 的值，安全地按逗号分隔成字符串列表。
	 *
	 * @param map       源 map（如数据库配置）
	 * @param key       要获取的 key（如 "panel_names"）
	 * @return          分割后的 List<String>，如果 map 为 null 或 key 不存在/为空，返回空列表
	 */
	public static List<String> getCommaSeparatedList(Map<String, Object> map, String key) {
		if (map == null || key == null) {
			return Collections.emptyList();
		}

		Object value = map.get(key);
		if (value == null) {
			return Collections.emptyList();
		}

		String str = value.toString().trim();
		if (str.isEmpty()) {
			return Collections.emptyList();
		}

		return Arrays.asList(str.split("\\s*,\\s*")); // 自动 trim 掉逗号两边的空格
	}
}
