package com.novo.report.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class LogUtils {

	// 定义 logger 名称和日志文件名
	private static final String LOGGER_NAME = "ReportErrorLogger";
	private static final String LOG_FILE_NAME = "report_error.log"; // 只保留文件名
	private static final int MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
	private static final int MAX_BACKUP_INDEX = 5;

	// 动态获取 Tomcat logs 目录路径
	private static final String LOG_DIR = getTomcatLogsPath();
	private static final String LOG_FILE_PATH = LOG_DIR + File.separator + LOG_FILE_NAME;

	// 静态初始化 logger
	private static final Logger reportErrorLogger = initLogger();

	private static String getTomcatLogsPath() {
		String catalinaHome = System.getProperty("catalina.home");
		if (catalinaHome == null) {
			// 开发环境或未设置 catalina.home 时的默认处理
			return "." + File.separator + "logs";
		}
		return catalinaHome + File.separator + "logs";
	}

	private static Logger initLogger() {
		Logger logger = Logger.getLogger(LOGGER_NAME);

		try {
			// 使用动态路径创建 FileHandler
			FileHandler fileHandler = new FileHandler(
					LOG_FILE_PATH, MAX_FILE_SIZE, MAX_BACKUP_INDEX, true
			);

			fileHandler.setFormatter(new Formatter() {
				@Override
				public String format(LogRecord record) {
					StringBuilder sb = new StringBuilder();
					sb.append("[" + new Date(record.getMillis()) + "] ");
					sb.append("[" + record.getLevel().getName() + "] ");
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
			logger.setLevel(Level.SEVERE);
			logger.setUseParentHandlers(false);

		} catch (IOException e) {
			System.err.println("Failed to initialize log handler: " + e.getMessage());
			e.printStackTrace();
		}

		return logger;
	}

	/**
	 * 获取专用于报告错误的日志记录器
	 */
	public static Logger getReportErrorLogger() {
		return reportErrorLogger;
	}

	/**
	 * 获取当前日志文件路径（调试用）
	 */
	public static String getCurrentLogFilePath() {
		return LOG_FILE_PATH;
	}
}
