package com.novo.report.task.queue;


import com.google.gson.Gson;
import com.novo.report.common.redis.JedisUtils;
import com.novo.report.utils.LogUtils;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 子报告生成任务生产者
 * 负责将子报告生成任务提交到Redis队列，供消费者异步处理
 */
public class SubreportProducer {

    private static final String SUBREPORT_QUEUE_KEY = "subreport_generation_queue";
    private static final Logger logger = LogUtils.getLogger("SubreportTaskLogger");

    /**
     * 提交子报告生成任务到Redis队列
     *
     * @param taskId        任务ID
     * @param subreportPath 子报告路径
     * @param reportInfo    报告基本信息
     * @param reportDetail  报告详细信息
     */
    public void submitSubreportTask(Integer taskId, String subreportPath, String reportInfo, String reportDetail) {
        // 参数校验
        if (subreportPath == null || subreportPath.trim().isEmpty()) {
            throw new IllegalArgumentException("子报告路径(subreportPath)不能为空");
        }
        if (reportInfo == null || reportInfo.trim().isEmpty()) {
            throw new IllegalArgumentException("报告基本信息(reportInfo)不能为空");
        }
        if (reportDetail == null || reportDetail.trim().isEmpty()) {
            throw new IllegalArgumentException("报告详细信息(reportDetail)不能为空");
        }

        // 封装任务信息
        Map<String, String> task = new HashMap<>(4);
        task.put("task_id", String.valueOf(taskId));
        task.put("subreport_path", subreportPath);
        // task.put("report_info", reportInfo);
        // task.put("report_detail", reportDetail);
        task.put("create_time", String.valueOf(System.currentTimeMillis())); // 记录任务创建时间

        Jedis jedis = null;
        try {

            jedis = JedisUtils.getResource();
            Gson gson = new Gson();
            // 将任务序列化后加入队列
            String taskJson = gson.toJson(task);
            Long result = jedis.lpush(SUBREPORT_QUEUE_KEY, taskJson);

            // 记录任务提交日志
            if (result != null && result > 0) {
                logger.info("提交小报告生成任务成功，taskId: " + task.get("task_id") + "队列长度: " + result + "\n");

            } else {
                logger.info("提交小报告生成失败，taskId: " + task.get("task_id") + "\n");
            }
        } catch (Exception e) {
            logger.severe("提交小报告生成失败，taskId: " + task.get("task_id") + "\n" + e.getMessage() + "\n");
            e.printStackTrace();
        } finally {
            // 使用工具类关闭连接
            JedisUtils.close(jedis);
        }
    }

    /**
     * 生成唯一任务ID
     * 格式: 时间戳_随机数（避免并发冲突）
     */
    private String generateTaskId() {
        return System.currentTimeMillis() + "_" + (int) (Math.random() * 10000);
    }
}
