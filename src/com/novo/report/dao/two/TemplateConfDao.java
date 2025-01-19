package com.novo.report.dao.two;

import com.novo.report.beans.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface TemplateConfDao {
    /**
     * 获取模板配置信息
     * @param templateName
     * @return
     */
    TemplateConf get(String templateName);

    /**
     * 获取全部模板配置信息
     */
    List<TemplateConf> list();
}
