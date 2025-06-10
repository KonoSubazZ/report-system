package com.novo.report.interceptor;

import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.novo.report.utils.LogUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;


/**
 * MyBatis SQL 错误拦截器，仅在发生异常时打印 SQL 语句
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class})})
public class SqlErrorInterceptor implements Interceptor {

    // 使用工具类获取 specialLogger
    private static final Logger logger = LogUtils.getReportErrorLogger();


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } catch (Throwable t) {
            Statement statement = (Statement) invocation.getArgs()[0];
            String sql = statement.toString(); // 获取实际执行的 SQL
            // 使用 LogUtils 提供的 logger
            logger.log(Level.SEVERE, "SQL Error occurred:\n" + sql, t);

            throw t;
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 可以在这里读取配置参数
    }
}
