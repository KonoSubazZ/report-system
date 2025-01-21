package com.novo.report.service.impl;


import com.novo.report.dao.two.ModuleDao;
import com.novo.report.mod.ModProductDesc;
import com.novo.report.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleDao moduleDao;

    @Override
    public ModProductDesc getProductDesc(String templateName) {
        return moduleDao.getProductDesc(templateName);
    }
}
