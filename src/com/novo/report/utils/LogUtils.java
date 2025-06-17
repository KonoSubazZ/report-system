
package com.novo.report.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.*;

public class LogUtils {

	// 默认日志配置
	private static final int MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
	private static final int MAX_BACKUP_INDEX = 5;
	// private static final String LOG_DIR = getTomcatLogsPath();
	private static final String LOG_DIR = "/data/soft/apache-tomcat-8.5.43/logs";

	// 存储不同类型的 Logger
	private static final Map<String, Logger> loggerMap = new ConcurrentHashMap<>();

	/**
	 * 获取指定名称的 Logger 实例，若不存在则自动创建
	 */
	public static synchronized Logger getLogger(String loggerName) {
		return loggerMap.computeIfAbsent(loggerName, LogUtils::createLogger);
	}

	/**
	 * 创建一个新的 Logger 实例并绑定到指定的日志文件
	 */
	private static Logger createLogger(String loggerName) {
		Logger logger = Logger.getLogger(loggerName);


		// 移除旧的 Handler，避免重复写入和 .1 .lck 文件
		Handler[] existingHandlers = logger.getHandlers();
		for (Handler handler : existingHandlers) {
			logger.removeHandler(handler);
			try {
				handler.close();  // 关闭旧 Handler 的资源
			} catch (Exception e) {
				System.err.println("Failed to close old handler: " + e.getMessage());
			}
		}

		String logFileName = loggerName.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
		String logFilePath = LOG_DIR + File.separator + logFileName + ".log";

		try {
			// FileHandler fileHandler = new FileHandler(logFilePath, MAX_FILE_SIZE, MAX_BACKUP_INDEX, true);

			// 修改日志文件不自动拆分
			FileHandler fileHandler = new FileHandler(logFilePath, true);
			fileHandler.setFormatter(new Formatter() {
				@Override
				public String format(LogRecord record) {
					StringBuilder sb = new StringBuilder();
					sb.append("[").append(new Date(record.getMillis())).append("] ");
					sb.append("[").append(record.getLevel().getName()).append("] ");
					sb.append(record.getMessage()).append("\n");

					if (record.getThrown() != null) {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						record.getThrown().printStackTrace(pw);
						sb.append("Exception:\n").append(sw.toString());
					}

					return sb.toString();
				}
			});

			logger.addHandler(fileHandler);
			logger.setLevel(Level.ALL);
			logger.setUseParentHandlers(false);

		} catch (IOException e) {
			System.err.println("Failed to initialize log handler for " + loggerName + ": " + e.getMessage());
			e.printStackTrace();
		}

		return logger;
	}

	/**
	 * 动态获取 Tomcat logs 目录路径
	 */
	private static String getTomcatLogsPath() {
		String catalinaHome = System.getProperty("catalina.home");
		if (catalinaHome == null) {
			return "." + File.separator + "logs";
		}
		return catalinaHome + File.separator + "logs";
	}

	/**
	 * 快捷方法：记录信息日志
	 */
	public static void info(String loggerName, String message) {
		getLogger(loggerName).info(message);
	}

	/**
	 * 快捷方法：记录严重错误日志
	 */
	public static void severe(String loggerName, String message, Throwable thrown) {
		getLogger(loggerName).log(Level.SEVERE, message, thrown);
	}

	/**
	 * 获取当前日志目录（调试用）
	 */
	public static String getCurrentLogDirectory() {
		return LOG_DIR;
	}
}