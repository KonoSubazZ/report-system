package com.novo.report.common.redis;

/**
 * 测试类，验证Jedis工具类的使用
 */
public class JedisTest {
    public static void main(String[] args) {
        try {
            // 测试设置和获取值
            String setResult = JedisUtils.set("testKey", "Hello Jedis Utils");
            System.out.println("设置结果: " + setResult);

            String value = JedisUtils.get("testKey");
            System.out.println("获取值: " + value);

            // 测试设置带过期时间的值
            JedisUtils.setex("expireKey", 60, "This will expire in 60 seconds");
            System.out.println("过期键的值: " + JedisUtils.get("expireKey"));

            // 测试键是否存在
            boolean exists = JedisUtils.exists("testKey");
            System.out.println("testKey是否存在: " + exists);

            // 测试删除键
            Long delResult = JedisUtils.del("testKey");
            System.out.println("删除结果: " + delResult);

            exists = JedisUtils.exists("testKey");
            System.out.println("删除后testKey是否存在: " + exists);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 应用关闭时销毁连接池
            // JedisUtils.destroy();
        }
    }
}