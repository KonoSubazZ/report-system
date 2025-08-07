package com.novo.report.task.queue;


import com.google.gson.Gson;
import com.novo.report.common.redis.JedisUtils;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * 子报告生成任务生产者
 * 负责将子报告生成任务提交到Redis队列，供消费者异步处理
 */
public class SubreportProducer {

    private static final String SUBREPORT_QUEUE_KEY = "subreport_generation_queue";

    /**
     * 提交子报告生成任务到Redis队列
     *
     * @param subreportPath 子报告路径
     * @param reportInfo    报告基本信息
     * @param reportDetail  报告详细信息
     */
    public void submitSubreportTask(String subreportPath, String reportInfo, String reportDetail) {
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
        task.put("task_id", generateTaskId());
        task.put("subreport_path", subreportPath);
        task.put("report_info", reportInfo);
        task.put("report_detail", reportDetail);
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
                System.out.printf("子报告生成任务提交成功，taskId: %s, 队列长度: %d%n",
                        task.get("task_id"), result);
            } else {
                System.err.printf("子报告生成任务提交失败，taskId: %s%n", task.get("task_id"));
            }
        } catch (Exception e) {
            System.err.printf("提交子报告生成任务时发生异常，taskId: %s, 异常信息: %s%n",
                    task.get("task_id"), e.getMessage());
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
