package com.novo.report.utils;

/**
 * 此工具类用来暂时存放各种业务抽离的通用逻辑，不一定别的业务用到
 */
public class ServiceUtils {

    private ServiceUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String getAppellation(String gender) {
        if ("男".equals(gender)) {
            return "先生";
        } else if ("女".equals(gender)) {
            return "女士";
        } else {
            return "先生/女士";
        }
    }

    /**
     * 如果输入字符串为空或null，返回默认值
     */
    public static String defaultIfEmpty(String value, String defaultValue) {
        return (value == null || value.isEmpty()) ? defaultValue : value;
    }

    public static String removeTrailingDots(String str) {
        while (str.endsWith(".")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

}
