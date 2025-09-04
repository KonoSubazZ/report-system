package com.novo.report.service.impl;


import com.novo.report.dao.two.CustomDao;
import com.novo.report.service.CustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class CustomServiceImpl implements CustomService {

    @Autowired
    private CustomDao customDao;
    @Override
    public Map<String, Object> getCstoneTipInfo(String disease) {
        return customDao.getCstoneTipInfo(disease);
    }
}
