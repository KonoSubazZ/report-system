package com.novo.report.utils;

import java.util.Random;

public class RandomUtils {
    public static String getStringRandom(int length) {
        String val = "";
        Random randomUtils = new Random();

        // 参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {

            String charOrNum = randomUtils.nextInt(2) % 2 == 0 ? "char" : "num";
            // 输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 输出是大写字母还是小写字母
                int temp = randomUtils.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (randomUtils.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(randomUtils.nextInt(10));
            }
        }
        return val;
    }

    public static void main(String[] args) {
        // 测试
        for (int i = 0; i < 50; i++) {
            System.out.println(getStringRandom(4));

        }
    }
}
