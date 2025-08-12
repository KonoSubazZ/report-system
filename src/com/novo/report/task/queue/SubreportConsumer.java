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
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SubreportConsumer {

    private static final String SUBREPORT_QUEUE_KEY = "subreport_generation_queue";
    private static final Logger logger = LogUtils.getLogger("SubreportTaskLogger");
    private static final int THREAD_POOL_SIZE = 5;
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    //    private static final String GENERATE_SCRIPT_PATH = "/data/soft/scripts/generate_xbg.py";
    private static final String GENERATE_SCRIPT_PATH = "/data/soft/scripts/generate_xbg.py";
    private static final long SCRIPT_TIMEOUT_MS = 600 * 1000;
    private final SubreportGenerator reportGenerator = new SubreportGenerator();
    private volatile boolean isRunning = true;

    // 启动消费者
    public void start() {
        logger.info("小报告消费者启动，开始监听队列: " + SUBREPORT_QUEUE_KEY);
        new Thread(this::consumeTasks, "Subreport-Consumer-Main").start();
    }

    // 停止消费者
    public void stop() {
        isRunning = false;
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        logger.info("小报告消费者已停止");
    }

    /**
     * 核心消费逻辑：从Redis队列获取任务并处理
     * 适配生产者的lpush，使用brpop从右侧阻塞获取（FIFO顺序）
     */
    private void consumeTasks() {
        while (isRunning) {
            Jedis jedis = null;
            try {
                // 1. 获取Redis连接（每次循环重新获取，避免连接长时间占用）
                jedis = JedisUtils.getResource();
                if (jedis == null) {
                    logger.severe("获取Redis连接失败，1秒后重试");
                    Thread.sleep(1000);
                    continue;
                }

                // 2. 阻塞从队列右侧获取任务（超时1秒，避免空轮询）
                // brpop返回格式：[队列名, 消息内容]，若超时返回null
                List<String> result = jedis.brpop(1, SUBREPORT_QUEUE_KEY);

                // 3. 处理返回结果（关键：避免空指针）
                if (result == null || result.size() < 2) {
                    // 无消息或格式异常，继续循环等待
                    continue;
                }

                // 4. 提取任务JSON并提交到线程池处理
                String taskJson = result.get(1); // 索引1是消息内容
                if (taskJson != null && !taskJson.isEmpty()) {
                    logger.info("获取到任务: " + taskJson.substring(0, Math.min(50, taskJson.length())) + "...");
                    executorService.submit(() -> processTask(taskJson));
                }

            } catch (InterruptedException e) {
                // 线程被中断，退出循环
                logger.warning("消费线程被中断: " + e.getMessage());
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                logger.severe("消费任务异常: " + e.getMessage());
                try {
                    // 发生异常时休眠1秒，避免频繁报错
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } finally {
                // 5. 确保连接释放（关键：避免连接池耗尽）
                if (jedis != null) {
                    try {
                        JedisUtils.close(jedis);
                    } catch (Exception e) {
                        logger.warning("关闭Redis连接失败: " + e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * 处理单个任务：解析JSON并生成报告
     */
    private void processTask(String taskJson) {
        try {
            // 解析任务JSON为Map
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> task = gson.fromJson(taskJson, type);

            // 提取任务参数（与生产者提交的字段对应）
            String taskId = task.get("task_id");
            String subreportPath = task.get("subreport_path");
            // String reportInfo = task.get("report_info");
            // String reportDetail = task.get("report_detail");

            // 校验参数
            if (taskId == null || subreportPath == null) {
                logger.warning("任务参数不完整，taskId: " + taskId);
                return;
            }

            long startTime = System.currentTimeMillis();

            logger.info("开始处理任务，taskId: " + taskId + "，开始时间: " + new Date(startTime));
            // 调用生成器生成报告
            boolean success = reportGenerator.generate(taskId, subreportPath);

            long endTime = System.currentTimeMillis();
            double costSeconds = (endTime - startTime) / 1000.0;
            if (success) {
                logger.info("任务处理成功，taskId: " + taskId +
                        "，结束时间: " + new Date(endTime) +
                        "，耗时: " + costSeconds + "s");
            } else {
                logger.severe(String.format(
                        "任务处理失败，taskId: %s，结束时间: %s，耗时: %.2f秒",
                        taskId,
                        new Date(endTime),
                        costSeconds
                ));
            }

        } catch (Exception e) {
            logger.severe("处理任务异常，taskJson: " + taskJson + ", 错误: " + e.getMessage());
        }
    }

    // 报告生成器（实际业务逻辑）
//    private class SubreportGenerator {
//        public boolean generate(String taskId, String subreportPath, String reportInfo, String reportDetail) {
//            try {
//                // 1. 解析reportInfo和reportDetail（JSON字符串转对象）
//                // 示例：ReportInfo info = new Gson().fromJson(reportInfo, ReportInfo.class);
//
//                // 2. 执行报告生成逻辑（如生成文件、写入数据库等）
//                logger.info("生成报告: " + subreportPath + " (taskId: " + taskId + ")");
//
//                // 3. 实际业务处理（此处省略具体实现）
//                return true;
//            } catch (Exception e) {
//                logger.severe("生成报告失败，taskId: " + taskId + ", 错误: " + e.getMessage());
//                return false;
//            }
//        }
//    }

    private class SubreportGenerator {

        /**
         * 调用Python脚本生成报告
         */
        public boolean generate(String taskId, String subreportPath) {
            // 构建命令：python 脚本路径 子报告路径 报告信息 报告详情
//            String[] command = {
//                    "python",
//                    GENERATE_SCRIPT_PATH,
//                    subreportPath,
//                    reportInfo,
//                    reportDetail
//            };
            String[] command = {
                    "python",
                    GENERATE_SCRIPT_PATH,
                    taskId
            };

            Process process = null;
            try {
                logger.info("开始调用Python脚本，taskId: " + taskId + "，命令: " + String.join(" ", command));

                // 启动进程执行脚本
                ProcessBuilder processBuilder = new ProcessBuilder(command);
                // 合并错误流到输入流，便于统一处理
                processBuilder.redirectErrorStream(true);
                process = processBuilder.start();

                // 异步读取脚本输出（避免阻塞）
                StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), taskId);
                new Thread(outputGobbler).start();

                // 等待脚本执行完成，设置超时时间
                boolean completed = process.waitFor(SCRIPT_TIMEOUT_MS, TimeUnit.MILLISECONDS);

                if (!completed) {
                    // 超时：销毁进程
                    process.destroyForcibly();
                    logger.severe("调用Python脚本超时，已强制终止，taskId: " + taskId);
                    return false;
                }

                // 检查脚本退出码（0表示成功，非0表示失败）
                int exitCode = process.exitValue();
                if (exitCode == 0) {
                    logger.info("Python脚本执行成功，taskId: " + taskId);
                    return true;
                } else {
                    logger.severe("Python脚本执行失败，退出码: " + exitCode + "，taskId: " + taskId);
                    return false;
                }

            } catch (IOException e) {
                logger.severe("调用Python脚本时发生IO错误，taskId: " + taskId + "，错误: " + e.getMessage());
                return false;
            } catch (InterruptedException e) {
                logger.severe("调用Python脚本被中断，taskId: " + taskId + "，错误: " + e.getMessage());
                Thread.currentThread().interrupt(); // 保留中断状态
                return false;
            } finally {
                // 确保进程资源释放
                if (process != null) {
                    process.destroyForcibly();
                }
            }
        }

        /**
         * 用于异步读取进程输出流的工具类
         * 避免因输出缓冲区满导致进程阻塞
         */
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
                        // 记录Python脚本的输出日志（便于调试）
                        logger.info("Python脚本输出 [taskId=" + taskId + "]: " + line);
                    }
                } catch (IOException e) {
                    logger.warning("读取Python脚本输出失败，taskId: " + taskId + "，错误: " + e.getMessage());
                }
            }
        }
    }
}
