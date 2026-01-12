package com.novo.report.common.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Jedis工具类，管理连接池和提供Redis操作方法
 */
public class JedisUtils {
    // 连接池实例
    private static JedisPool jedisPool;

    // 静态初始化块，初始化连接池
    /*
    static {
        try {
            // 创建连接池配置对象
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            // 设置连接池参数
            poolConfig.setMaxTotal(JedisConfig.getMaxTotal());
            poolConfig.setMaxIdle(JedisConfig.getMaxIdle());
            poolConfig.setMinIdle(JedisConfig.getMinIdle());
            // poolConfig.setMaxWaitMillis(JedisConfig.getMaxWaitMillis());
            poolConfig.setTestOnBorrow(JedisConfig.isTestOnBorrow());

            // 创建连接池
            String password = JedisConfig.getPassword();
            if (password == null || password.trim().isEmpty()) {
                // 无密码连接
                jedisPool = new JedisPool(poolConfig,
                        JedisConfig.getHost(),
                        JedisConfig.getPort(),
                        JedisConfig.getTimeout());
            } else {
                // 带密码连接
                jedisPool = new JedisPool(poolConfig,
                        JedisConfig.getHost(),
                        JedisConfig.getPort(),
                        JedisConfig.getTimeout(),
                        password);
            }

            // 测试连接是否成功
            try (Jedis jedis = getResource()) {
                String ping = jedis.ping();
                if (!"PONG".equals(ping)) {
                    throw new RuntimeException("Redis连接测试失败");
                }
                System.out.println("Redis连接池初始化成功");
            }
        } catch (Exception e) {
            throw new RuntimeException("Redis连接池初始化失败", e);
        }
    }*/

    public static synchronized void init() {
        // 若连接池已存在且未关闭，直接返回（避免重复创建，提高效率）
        if (jedisPool != null && !jedisPool.isClosed()) {
            System.out.println("Redis连接池已存在且可用，无需重复初始化");
            return;
        }

        try {
            // 1. 构建连接池配置（与原有JedisUtils逻辑一致，保证配置兼容）
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(JedisConfig.getMaxTotal());
            poolConfig.setMaxIdle(JedisConfig.getMaxIdle());
            poolConfig.setMinIdle(JedisConfig.getMinIdle());
            poolConfig.setTestOnBorrow(JedisConfig.isTestOnBorrow());

            // 2. 获取连接配置（与原有JedisUtils逻辑一致，无改动）
            String password = JedisConfig.getPassword();
            String host = JedisConfig.getHost();
            int port = JedisConfig.getPort();
            int timeout = JedisConfig.getTimeout();

            // 3. 创建连接池
            if (password == null || password.trim().isEmpty()) {
                jedisPool = new JedisPool(poolConfig, host, port, timeout);
            } else {
                jedisPool = new JedisPool(poolConfig, host, port, timeout, password);
            }

            // 4. 测试连接可用性（可选，与原有逻辑一致，失败则抛出异常）
            try (Jedis jedis = jedisPool.getResource()) {
                String ping = jedis.ping();
                if (!"PONG".equals(ping)) {
                    throw new RuntimeException("Redis连接测试失败，响应：" + ping);
                }
            }

            System.out.println("Redis连接池初始化/重新初始化成功");
        } catch (Exception e) {
            // 清理无效连接池，避免残留无效实例
            if (jedisPool != null) {
                jedisPool.destroy();
                jedisPool = null;
            }
            throw new RuntimeException("Redis连接池初始化失败", e);
        }
    }

    /**
     * 从连接池获取Jedis实例
     */
    public static Jedis getResource() {
        if (jedisPool == null || jedisPool.isClosed()) {
            if (jedisPool == null || jedisPool.isClosed()) {
                init(); // 自动触发初始化
            }
        }
        return jedisPool.getResource();
    }

    /**
     * 关闭Jedis连接（归还到连接池）
     */
    public static void close(Jedis jedis) {
        if (jedis != null) {
            try {
                jedis.close();
            } catch (Exception e) {
                System.err.println("关闭Jedis连接失败: " + e.getMessage());
            }
        }
    }

    /**
     * 销毁连接池（一般在应用关闭时调用）
     */
    public static void destroy() {
        if (jedisPool != null) {
            jedisPool.destroy();
            System.out.println("Redis连接池已销毁");
        }
    }

    // 以下是常用的Redis操作封装示例

    /**
     * 设置键值对
     */
    public static String set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.set(key, value);
        } finally {
            close(jedis);
        }
    }

    /**
     * 获取键对应的值
     */
    public static String get(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.get(key);
        } finally {
            close(jedis);
        }
    }

    /**
     * 设置带过期时间的键值对
     *
     * @param key     键
     * @param seconds 过期时间（秒）
     * @param value   值
     */
    public static String setex(String key, int seconds, String value) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.setex(key, seconds, value);
        } finally {
            close(jedis);
        }
    }

    /**
     * 删除键
     */
    public static Long del(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.del(key);
        } finally {
            close(jedis);
        }
    }

    /**
     * 判断键是否存在
     */
    public static Boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.exists(key);
        } finally {
            close(jedis);
        }
    }
}
