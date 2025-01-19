package com.novo.report.service.impl;




import com.novo.report.beans.TemplateConf;
import com.novo.report.dao.two.TemplateConfDao;
import com.novo.report.service.TemplateConfService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class TemplateConfServiceImpl implements TemplateConfService {

    @Autowired
    private TemplateConfDao templateConfDao;


    @Override
    public TemplateConf get(String templateName) {
        return templateConfDao.get(templateName);
    }

    @Override
    public List<TemplateConf> list() {
        return templateConfDao.list();
    }
}
