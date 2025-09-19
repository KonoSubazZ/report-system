package com.novo.report.service.impl;

import com.novo.report.beans.SpecimenHead;
import com.novo.report.dao.three.NewLimsSampleDao;
import com.novo.report.service.NewLimsSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewLimsSampleServiceImpl implements NewLimsSampleService {

    @Autowired
    private NewLimsSampleDao newLimsSampleDao;

    @Override
    public SpecimenHead getNewLimsSampleByBarcode(String barcode) {
        return newLimsSampleDao.getNewLimsSampleByBarcode(barcode);
    }

    @Override
    public List<SpecimenHead> getNewLimsSampleList() {
        return newLimsSampleDao.getNewLimsSampleList();
    }

    @Override
    public String mergedSpecimenType(String subbarcode,String  date, String productName) {
        return newLimsSampleDao.getSpecimenTyupeByBarcode(subbarcode + "T", date, productName);
    }

}
