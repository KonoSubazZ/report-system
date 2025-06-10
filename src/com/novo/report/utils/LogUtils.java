package com.novo.report.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class LogUtils {

	// 定义 logger 名称和日志文件路径
	private static final String LOGGER_NAME = "ReportErrorLogger";
	private static final String LOG_FILE_PATH = "/data/soft/apache-tomcat-8.5.43/logs/report_error.log";
	private static final int MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
	private static final int MAX_BACKUP_INDEX = 5;

	// 静态初始化 logger
	private static final Logger reportErrorLogger = initLogger();

	private static Logger initLogger() {
		Logger logger = Logger.getLogger(LOGGER_NAME);

		try {
			FileHandler fileHandler = new FileHandler(LOG_FILE_PATH, MAX_FILE_SIZE, MAX_BACKUP_INDEX, true);
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
}
