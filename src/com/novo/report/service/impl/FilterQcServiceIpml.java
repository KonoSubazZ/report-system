package com.novo.report.service.impl;

import com.novo.report.beans.FilterQc;
import com.novo.report.dao.two.FilterQcDao;
import com.novo.report.service.FilterQcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FilterQcServiceIpml implements FilterQcService {

    @Autowired
    private FilterQcDao filterQcDao;

    @Override
    public void updateQc(FilterQc filterQc) {
        if (filterQc.isFlag()) {
            filterQcDao.updateSampleFile(filterQc.getTumorcellcontent(),filterQc.getDNA_total(),filterQc.getDNA_degradation(),filterQc.getOutbound_quantity(),filterQc.getSubbarcode());
        }
        filterQcDao.updateQc(filterQc);
    }

    @Override
    public void updateQcRna(FilterQc filterQc) {
        filterQcDao.updateQcRna(filterQc);
    }

    @Override
    public void updateQcHrd(FilterQc filterQc) {
        filterQcDao.updateQcHrd(filterQc);
    }
}
