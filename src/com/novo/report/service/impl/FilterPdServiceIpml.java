package com.novo.report.service.impl;

import com.novo.report.beans.FilterPd;
import com.novo.report.dao.two.FilterPdDao;
import com.novo.report.service.FilterPdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FilterPdServiceIpml implements FilterPdService {

    @Autowired
    private FilterPdDao filterPdDao;

    @Override
    public FilterPd getPDInfo(String subbarcode, String analysis_date, String product_name) {
        return filterPdDao.getPDInfo(subbarcode, analysis_date, product_name);
    }

    @Override
    public void updatePd(FilterPd filterPd) {
        filterPdDao.updatePd(filterPd);
    }
}
