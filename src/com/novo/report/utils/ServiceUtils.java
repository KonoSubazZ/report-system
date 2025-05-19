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



}
