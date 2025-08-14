package com.novo.report.task.queue;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.novo.report.common.redis.JedisUtils;
import com.novo.report.utils.LogUtils;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SubreportConsumer {
    private static final String SUBREPORT_QUEUE_KEY = "subreport_generation_queue";
    private static final Logger logger = LogUtils.getLogger("SubreportTaskLogger");
    private static final String GENERATE_SCRIPT_PATH = "/data/soft/scripts/generate_xbg.py";
    private static final long SCRIPT_TIMEOUT_MS = 600 * 1000;
    private final SubreportGenerator reportGenerator = new SubreportGenerator();
    private volatile boolean isRunning = true;
    private final String instanceId;
    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


    public SubreportConsumer() {
        String instanceId1;
        // 初始化实例标识
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            String processId = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
            instanceId1 = hostName + ":" + processId;
        } catch (UnknownHostException e) {
            instanceId1 = "unknown-host:" + ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        }
        this.instanceId = instanceId1;
    }


    public void start() {
        logger.info(" 同步消费者启动，监听队列: " + SUBREPORT_QUEUE_KEY);
        // 启动消费主线程（同步处理，单线程执行）
        Thread consumeThread = new Thread(this::consumeTasks, "Subreport-Sync-Consumer-" + instanceId);
        consumeThread.setDaemon(false);
        consumeThread.start();
    }


    public void stop() {
        logger.info(" 开始停止同步消费者...");
        isRunning = false;
        logger.info(" 同步消费者已停止");
    }


    /**
     * 核心消费逻辑：同步处理，主线程直接处理任务
     */
    private void consumeTasks() {
        logger.info(" 同步消费循环启动...");

        while (isRunning) {
            // logger.info(" 重新获取任务中...");
            Jedis jedis = null;
            try {
                // 获取Redis连接（带重试）
                int connectRetry = 0;
                while (jedis == null && connectRetry < 3 && isRunning) {
                    jedis = JedisUtils.getResource();
                    if (jedis == null) {
                        connectRetry++;
                        logger.warning(" 获取Redis连接失败，重试: " + connectRetry + "/3");
                        Thread.sleep(1000);
                    }
                }

                if (jedis == null) {
                    logger.severe(" 连续3次获取连接失败，跳过本次循环");
                    continue;
                }

                // 阻塞获取任务（超时30秒）
                logger.info(" 等待队列任务中30秒...");
                List<String> result = jedis.brpop(30, SUBREPORT_QUEUE_KEY);

                if (result == null || result.size() < 2) {
                    continue; // 无任务，继续循环
                }

                // 提取任务（同步处理，不使用线程池）
                String taskJson = result.get(1);
                if (taskJson == null || taskJson.trim().isEmpty()) {
                    logger.warning(" 获取到空任务，忽略");
                    continue;
                }

                String taskSummary = taskJson.length() > 100 ? taskJson.substring(0, 100) + "..." : taskJson;
                logger.info(" 获取到新任务: " + taskSummary);

                // 同步处理任务（主线程直接执行，不异步）
                processTask(taskJson);

            } catch (InterruptedException e) {
                logger.info(" 消费线程被中断，准备退出");
                Thread.currentThread().interrupt();
                break;
            } catch (Throwable t) {
                logger.severe(" 消费循环错误: " + t.getMessage());
                t.printStackTrace();
                try {
                    Thread.sleep(2000); // 出错后稍等再重试
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } finally {
                // 释放连接
                if (jedis != null) {
                    try {
                        JedisUtils.close(jedis);
                    } catch (Exception e) {
                        logger.warning(" 关闭连接失败: " + e.getMessage());
                    }
                }
            }
        }

        logger.info(" 同步消费循环已退出");
    }


    /**
     * 同步处理单个任务（在消费主线程中执行）
     */
    private void processTask(String taskJson) {
        String taskId = "unknown";
        try {
            // 解析任务
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> task = gson.fromJson(taskJson, type);

            taskId = task.getOrDefault("task_id", "unknown");
            String subreportPath = task.get("subreport_path");

            if (taskId == null || subreportPath == null) {
                logger.warning(" 任务参数不完整，taskId: " + taskId);
                return;
            }

            // 处理任务（同步执行，主线程会阻塞直到完成）
            long startTime = System.currentTimeMillis();
            logger.info(" 开始处理任务，taskId: " + taskId + "，开始时间: " + TIME_FORMATTER.format(new Date(startTime)));

            boolean success = reportGenerator.generate(taskId, subreportPath);

            long endTime = System.currentTimeMillis();
            double costSeconds = (endTime - startTime) / 1000.0;
            logger.info(" 任务处理完成，taskId: " + taskId +
                    "，耗时: " + String.format("%.2f", costSeconds) + "秒，成功: " + success);

        } catch (Throwable t) {
            logger.severe(" 处理任务异常，taskId: " + taskId + "，错误: " + t.getMessage());
            t.printStackTrace();
        }
    }


    /**
     * 报告生成器（逻辑不变，同步执行）
     */
    private class SubreportGenerator {
        public boolean generate(String taskId, String subreportPath) {
            String[] command = {"python", GENERATE_SCRIPT_PATH, taskId};
            Process process = null;

            try {
                logger.info(" 启动Python脚本，taskId: " + taskId + "，命令: " + String.join(" ", command));

                ProcessBuilder processBuilder = new ProcessBuilder(command);
                processBuilder.redirectErrorStream(true);
                process = processBuilder.start();

                // 同步读取脚本输出（因为是单线程，这里也用同步读取）
                // 注意：如果脚本输出量大，同步读取可能阻塞，可保留异步读取
                StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), taskId);
                new Thread(outputGobbler, "Script-Output-" + taskId).start();

                boolean completed = process.waitFor(SCRIPT_TIMEOUT_MS, TimeUnit.MILLISECONDS);

                if (!completed) {
                    process.destroyForcibly();
                    logger.severe(" 脚本超时，taskId: " + taskId);
                    return false;
                }

                int exitCode = process.exitValue();
                if (exitCode == 0) {
                    logger.info(" 脚本执行成功，taskId: " + taskId);
                    return true;
                } else {
                    logger.severe(" 脚本执行失败，exitCode: " + exitCode + "，taskId: " + taskId);
                    return false;
                }

            } catch (Exception e) {
                logger.severe(" 调用脚本异常，taskId: " + taskId + "，错误: " + e.getMessage());
                return false;
            } finally {
                if (process != null) {
                    process.destroyForcibly();
                }
            }
        }


        private class StreamGobbler implements Runnable {
            private final InputStream inputStream;
            private final String taskId;

            public StreamGobbler(InputStream inputStream, String taskId) {
                this.inputStream = inputStream;
                this.taskId = taskId;
            }

            @Override
            public void run() {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        logger.info(" Python输出 [taskId=" + taskId + "]: " + line);
                    }
                } catch (IOException e) {
                    logger.warning(" 读取脚本输出失败，taskId: " + taskId);
                }
            }
        }
    }


    public static void main(String[] args) {
        SubreportConsumer consumer = new SubreportConsumer();
        consumer.start();

        // 注册关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::stop));
    }
}
