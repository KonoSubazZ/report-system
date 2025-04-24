package com.novo.report.service;

import com.novo.report.beans.TemplateConf;

import java.util.List;


public interface TemplateConfService {

    /**
     * 获取模板配置信息
     * @param templateName
     * @return
     */
    TemplateConf get(String templateName);

    /**
     * 获取全部模板配置信息
     * @return
     */
    List<TemplateConf> list();
}

