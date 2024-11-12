package com.novo.report.service.impl;

import com.novo.report.beans.DataFileStatus;
import com.novo.report.dao.two.DataFileStatusDao;
import com.novo.report.dao.two.SendEmailDao;
import com.novo.report.service.DataFileStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataFileStatusServiceImpl implements DataFileStatusService {

    @Autowired
    private DataFileStatusDao dataFileStatusDao;

    @Override
    public void saveDataFileStatus(DataFileStatus dataFileStatus) {
        dataFileStatusDao.saveDataFileStatus(dataFileStatus);
    }
}
