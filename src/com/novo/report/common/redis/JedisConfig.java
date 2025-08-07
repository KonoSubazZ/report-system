package com.novo.report.common.redis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Redis配置类，用于加载配置文件
 */
public class JedisConfig {
    private static final Properties properties = new Properties();

    static {
        // 加载配置文件
        try (InputStream inputStream = JedisConfig.class.getClassLoader().getResourceAsStream("redis.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("找不到redis.properties配置文件");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("加载redis.properties配置文件失败", e);
        }
    }

    /**
     * 获取配置值
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * 获取整数类型的配置值
     */
    public static int getIntProperty(String key) {
        String value = getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return 0;
        }
        return Integer.parseInt(value.trim());
    }

    /**
     * 获取Redis主机地址
     */
    public static String getHost() {
        return getProperty("redis.host");
    }

    /**
     * 获取Redis端口号
     */
    public static int getPort() {
        return getIntProperty("redis.port");
    }

    /**
     * 获取Redis密码
     */
    public static String getPassword() {
        return getProperty("redis.password");
    }

    /**
     * 获取连接超时时间
     */
    public static int getTimeout() {
        return getIntProperty("redis.timeout");
    }

    /**
     * 获取最大连接数
     */
    public static int getMaxTotal() {
        return getIntProperty("redis.maxTotal");
    }

    /**
     * 获取最大空闲连接数
     */
    public static int getMaxIdle() {
        return getIntProperty("redis.maxIdle");
    }

    /**
     * 获取最小空闲连接数
     */
    public static int getMinIdle() {
        return getIntProperty("redis.minIdle");
    }

    /**
     * 获取最大等待毫秒数
     */
    public static int getMaxWaitMillis() {
        return getIntProperty("redis.maxWaitMillis");
    }

    /**
     * 是否在获取连接时检查有效性
     */
    public static boolean isTestOnBorrow() {
        return "true".equalsIgnoreCase(getProperty("redis.testOnBorrow"));
    }
}

